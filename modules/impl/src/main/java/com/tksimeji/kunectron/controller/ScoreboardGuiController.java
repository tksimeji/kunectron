package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.ScoreboardGui;
import com.tksimeji.kunectron.controller.impl.GuiControllerImpl;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiInitEventImpl;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiPlayerAddEvent;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiPlayerRemoveEvent;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiTickEventImpl;
import com.tksimeji.kunectron.markupextensions.context.Context;
import com.tksimeji.kunectron.type.ScoreboardGuiType;
import com.tksimeji.kunectron.util.Components;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ScoreboardGuiController extends GuiControllerImpl implements TickableGuiController {
    public static @Nullable ScoreboardGuiController lookup(final @NotNull Player player) {
        return Kunectron.getGuiControllers(ScoreboardGuiType.instance()).stream()
                .filter(controller -> controller.isPlayer(player))
                .findFirst()
                .orElse(null);
    }

    private static final @NotNull ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    private final @NotNull Scoreboard scoreboard = scoreboardManager.getNewScoreboard();;
    private final @NotNull Objective objective;

    private final @NotNull Set<Player> players = new HashSet<>();

    private @NotNull Component title;

    private final @NotNull List<ScoreboardLine> scoreboardLines = new ArrayList<>();

    private final boolean autoUpdate;
    private final boolean markupExtensions;

    public ScoreboardGuiController(final @NotNull Object gui, final @NotNull ScoreboardGui annotation) {
        super(gui);

        title = getDeclarationOrDefault(gui, ScoreboardGui.Title.class, ComponentLike.class, Component.empty()).getKey().asComponent();
        objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title.asComponent());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        autoUpdate = annotation.autoUpdate();
        markupExtensions = annotation.markupExtensions();

        final Map<Integer, ComponentLike> lineMap = new TreeMap<>();
        final List<ComponentLike> lineList = new ArrayList<>();

        for (final Pair<ComponentLike, ScoreboardGui.Line> declaration : getDeclarations(gui, ScoreboardGui.Line.class, ComponentLike.class)) {
            ScoreboardGui.Line lineAnnotation = declaration.getRight();
            if (lineAnnotation.index() != -1) {
                lineMap.put(lineAnnotation.index(), declaration.getLeft());
            } else {
                lineList.add(declaration.getLeft());
            }
        }

        for (final Map.Entry<Integer, ComponentLike> line : lineMap.entrySet()) {
            setLine(line.getKey(), line.getValue());
        }

        for (final ComponentLike line : lineList) {
            addLine(line);
        }
    }

    @Override
    public void init() {
        callEvent(new ScoreboardGuiInitEventImpl(gui));
    }

    public @NotNull Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    public void addPlayer(final @NotNull Player player) {
        if (isPlayer(player)) {
            return;
        }

        Optional.ofNullable(ScoreboardGuiController.lookup(player)).ifPresent(oldController -> oldController.removePlayer(player));

        players.add(player);
        player.setScoreboard(scoreboard);
        callEvent(new ScoreboardGuiPlayerAddEvent(gui, player));
    }

    public void removePlayer(final @NotNull Player player) {
        if (!isPlayer(player)) {
            return;
        }

        players.remove(player);
        player.setScoreboard(scoreboardManager.getMainScoreboard());
        callEvent(new ScoreboardGuiPlayerRemoveEvent(gui, player));
    }

    public boolean isPlayer(final @NotNull Player player) {
        return players.contains(player);
    }

    public @NotNull Component getTitle() {
        return title;
    }

    public void setTitle(final @NotNull ComponentLike title) {
        this.title = title.asComponent();
        objective.displayName(this.title.asComponent());
    }

    public @Nullable Component getLine(final int index) {
        if (index >= scoreboardLines.size()) {
            return null;
        }
        final ScoreboardLine line = scoreboardLines.get(index);
        return line.getComponent();
    }

    public @NotNull List<Component> getLines() {
        return scoreboardLines.stream().map(ScoreboardLine::getComponent).toList();
    }

    public void setLine(final int index, final @NotNull ComponentLike line) {
        if (index >= scoreboardLines.size()) {
            for (int i = scoreboardLines.size(); i <= index; i++) {
                ScoreboardLine scoreboardLine = new ScoreboardLine(Component.empty());
                if (i == index) {
                    scoreboardLine.setComponent(line.asComponent());
                }
                scoreboardLines.add(scoreboardLine);
            }
            updateScores();
        } else {
            scoreboardLines.get(index).setComponent(line.asComponent());
        }
    }

    public void addLine(final @NotNull ComponentLike line) {
        setLine(scoreboardLines.size(), line);
    }

    public void removeLine(final int index) {
        if (index >= scoreboardLines.size()) {
            return;
        }
        scoreboardLines.get(index).remove();
    }

    public void removeLines() {
        for (int i = scoreboardLines.size() - 1; i >= 0; i--) {
            removeLine(i);
        }
    }

    public void insertLine(final int index, final @NotNull ComponentLike line) {
        scoreboardLines.add(index, new ScoreboardLine(line.asComponent()));
        updateScores();
    }

    public void clearLine(final int index) {
        if (index < 0 || index >= scoreboardLines.size()) {
            return;
        }
        setLine(index, Component.empty());
    }

    public void clearLines() {
        for (int i = 0; i < scoreboardLines.size(); i++) {
            clearLine(i);
        }
    }

    public int getSize() {
        return scoreboardLines.size();
    }

    public void close() {
        Kunectron.deleteGuiController(this);
        objective.unregister();
        for (final Player player : getPlayers()) {
            removePlayer(player);
        }
    }

    @Override
    public void tick() {
        callEvent(new ScoreboardGuiTickEventImpl(gui));
        if (!autoUpdate) return;

        final Component title = markupExtensions ? Components.markupExtensions(this.title, markupExtensionContext) : this.title;

        if (!this.title.equals(title)) {
            objective.displayName(title);
        }

        for (final ScoreboardLine line : scoreboardLines) {
            if (!Objects.equals(line.createComponent(), line.getRendered())) {
                line.render();
            }
        }
    }

    private void updateScores() {
        for (final ScoreboardLine scoreboardLine : scoreboardLines) {
            scoreboardLine.updateScore();
        }
    }

    private final class ScoreboardLine {
        private @NotNull Component component;

        private final @NotNull Score score;

        private final @NotNull UUID uuid = UUID.randomUUID();

        private final @Nullable Context<?> ctx;

        public ScoreboardLine(final @Nullable Component component) {
            this(component, markupExtensions ? markupExtensionContext : null);
        }

        public ScoreboardLine(final @Nullable Component component, @Nullable final Context<?> ctx) {
            score = objective.getScore(uuid.toString());
            this.component = Optional.ofNullable(component).orElse(Component.empty());
            this.ctx = ctx;
            render();
        }

        public int getIndex() {
            return scoreboardLines.indexOf(this);
        }

        public @NotNull Component getComponent() {
            return component;
        }

        public void setComponent(final @NotNull Component component) {
            this.component = component;
        }

        public @NotNull Component createComponent() {
            return ctx != null ? Components.markupExtensions(component, ctx) : component;
        }

        public @NotNull Score getScore() {
            return score;
        }

        public void updateScore() {
            score.setScore(scoreboardLines.size() - 1 - getIndex());
        }

        public @Nullable Component getRendered() {
            return score.customName();
        }

        public void render() {
            score.customName(createComponent());
        }

        public void remove() {
            scoreboard.resetScores(uuid.toString());
            scoreboardLines.remove(this);
            updateScores();
        }
    }
}

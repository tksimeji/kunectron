package com.tksimeji.kunectron.controller;

import com.tksimeji.kunectron.Kunectron;
import com.tksimeji.kunectron.ScoreboardGui;
import com.tksimeji.kunectron.element.ComponentElement;
import com.tksimeji.kunectron.element.Element;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiInitEventImpl;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiPlayerAddEvent;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiPlayerRemoveEvent;
import com.tksimeji.kunectron.event.scoreboard.ScoreboardGuiTickEventImpl;
import com.tksimeji.kunectron.type.ScoreboardGuiType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ScoreboardGuiController extends AbstractGuiController implements TickableGuiController {
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

    private @NotNull ComponentElement title;

    private final @NotNull List<ScoreboardLine> scoreboardLines = new ArrayList<>();

    public ScoreboardGuiController(final @NotNull Object gui) {
        super(gui);

        title = Element.component(getDeclarationOrDefault(gui, ScoreboardGui.Title.class, ComponentLike.class, Component.empty()).getKey().asComponent(), markupExtensionContext);
        objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title.asComponent());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final Map<Integer, ComponentLike> lineMap = new TreeMap<>();
        final List<ComponentLike> lineList = new ArrayList<>();

        for (final Pair<ComponentLike, ScoreboardGui.Line> declaration : getDeclarations(gui, ScoreboardGui.Line.class, ComponentLike.class)) {
            ScoreboardGui.Line annotation = declaration.getRight();
            if (annotation.index() != -1) {
                lineMap.put(annotation.index(), declaration.getLeft());
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
        return title.asComponent();
    }

    public void setTitle(final @NotNull ComponentLike title) {
        this.title = Element.component(title, markupExtensionContext);
        objective.displayName(this.title.asComponent());
    }

    public @Nullable Component getLine(final int index) {
        if (index >= scoreboardLines.size()) {
            return null;
        }

        final ScoreboardLine line = scoreboardLines.get(index);
        return line.getElement().create();
    }

    public @NotNull List<Component> getLines() {
        return scoreboardLines.stream().map(line -> line.getElement().create()).toList();
    }

    public void setLine(final int index, final @NotNull ComponentLike line) {
        final ComponentElement element = Element.component(line.asComponent(), markupExtensionContext);

        if (index >= scoreboardLines.size()) {
            for (int i = scoreboardLines.size(); i <= index; i++) {
                ScoreboardLine scoreboardLine = new ScoreboardLineImpl();

                if (i == index) {
                    scoreboardLine.setElement(element);
                }

                scoreboardLines.add(scoreboardLine);
            }

            updateScores();
        } else {
            scoreboardLines.get(index).setElement(element);
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
        for (int i = 0; i < scoreboardLines.size(); i++) {
            removeLine(i);
        }
    }

    public void insertLine(final int index, final @NotNull ComponentLike line) {
        scoreboardLines.set(index, new ScoreboardLineImpl(line));
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

        if (objective.displayName().equals(getTitle())) {
            objective.displayName(getTitle());
        }

        for (final ScoreboardLine line : scoreboardLines) {
            if (!Objects.equals(line.getElement().create(), line.getRendered())) {
                line.render();
            }
        }
    }

    private void updateScores() {
        for (final ScoreboardLine scoreboardLine : scoreboardLines) {
            scoreboardLine.updateScore();
        }
    }

    private interface ScoreboardLine {
        int getIndex();

        @NotNull Score getScore();

        void updateScore();

        @NotNull ComponentElement getElement();

        void setElement(final @Nullable ComponentElement element);

        @Nullable Component getRendered();

        void render();

        void remove();
    }

    private final class ScoreboardLineImpl implements ScoreboardLine {
        private @NotNull ComponentElement element;

        private final @NotNull Score score;

        private final @NotNull UUID uuid = UUID.randomUUID();

        public ScoreboardLineImpl() {
            this(null);
        }

        public ScoreboardLineImpl(final @Nullable ComponentLike component) {
            this(component != null ? Element.component(component) : null);
        }

        public ScoreboardLineImpl(final @Nullable ComponentElement element) {
            score = objective.getScore(uuid.toString());
            this.element = element != null ? element : Element.component();
            render();
        }

        @Override
        public int getIndex() {
            return scoreboardLines.indexOf(this);
        }

        @Override
        public @NotNull Score getScore() {
            return score;
        }

        @Override
        public void updateScore() {
            score.setScore(scoreboardLines.size() - 1 - getIndex());
        }

        @Override
        public @NotNull ComponentElement getElement() {
            return element;
        }

        @Override
        public void setElement(final @Nullable ComponentElement element) {
            this.element = element != null ? element : Element.component();
        }

        @Override
        public @Nullable Component getRendered() {
            return score.customName();
        }

        @Override
        public void render() {
            score.customName(element.create());
        }

        @Override
        public void remove() {
            scoreboard.resetScores(uuid.toString());
            scoreboardLines.remove(this);

            updateScores();
        }
    }
}

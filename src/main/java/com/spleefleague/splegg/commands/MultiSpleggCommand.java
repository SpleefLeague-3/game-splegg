/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spleefleague.splegg.commands;

import com.google.common.collect.Sets;
import com.spleefleague.core.Core;
import com.spleefleague.core.chat.Chat;
import com.spleefleague.core.command.CoreCommand;
import com.spleefleague.core.command.annotation.*;
import com.spleefleague.core.game.Arena;
import com.spleefleague.core.game.BattleMode;
import com.spleefleague.core.game.arena.Arenas;
import com.spleefleague.core.player.CorePlayer;
import com.spleefleague.core.player.rank.CoreRank;
import com.spleefleague.core.util.CoreUtils;
import com.spleefleague.splegg.Splegg;
import com.spleefleague.splegg.game.SpleggMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author NickM13
 */
public class MultiSpleggCommand extends CoreCommand {

    public MultiSpleggCommand() {
        super("splegg", CoreRank.DEFAULT);
        this.addAlias("sg");
        this.setOptions("gamemodes", pi -> CoreUtils.enumToStrSet(SpleggMode.class, true));
        this.setOptions("arenas", this::getArenas);
        this.setOptions("players", this::getPlayers);
        this.setContainer("splegg");
    }


    protected SortedSet<String> getArenas(PriorInfo pi) {
        BattleMode mode = SpleggMode.valueOf(pi.getArgs().get(pi.getArgs().size() - 1).toUpperCase()).getBattleMode();
        if (mode.getTeamStyle() == BattleMode.TeamStyle.TEAM ||
                mode.getTeamStyle() == BattleMode.TeamStyle.VERSUS ||
                mode.getTeamStyle() == BattleMode.TeamStyle.SOLO) {
            return Sets.newTreeSet(Arenas.getUnpaused(mode).keySet());
        }
        return new TreeSet<>();
    }

    protected SortedSet<String> getPlayers(PriorInfo pi) {
        SortedSet<String> players = Splegg.getInstance().getPlayers().getAllNames();
        for (int i = 2; i < pi.getArgs().size(); i++) {
            players.remove(pi.getArgs().get(i));
        }
        String mode = pi.getArgs().get(pi.getArgs().size() - 1);
        return Sets.newTreeSet(Arenas.getUnpaused(SpleggMode.valueOf(mode.toUpperCase()).getBattleMode()).keySet());
    }

    @CommandAnnotation
    public void multisplegg(CorePlayer sender,
                            @Nullable @OptionArg(listName = "arenas") String arena) {
        Splegg.getInstance().queuePlayer(SpleggMode.MULTI.getBattleMode(), sender, Arenas.get(arena, SpleggMode.MULTI.getBattleMode()));
    }

}

package com.robertx22.mine_and_slash.vanilla_mc.new_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.robertx22.mine_and_slash.capability.player.PlayerData;
import com.robertx22.mine_and_slash.database.data.game_balance_config.PlayerPointsType;
import com.robertx22.mine_and_slash.database.data.profession.Profession;
import com.robertx22.mine_and_slash.database.registry.ExileRegistryTypes;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.localization.Chats;
import com.robertx22.mine_and_slash.vanilla_mc.new_commands.parts.ResetPlayerData;
import com.robertx22.mine_and_slash.vanilla_mc.new_commands.wrapper.*;
import com.robertx22.mine_and_slash.vanilla_mc.packets.OpenGuiPacket;
import com.robertx22.library_of_exile.main.Packets;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerCommands {

    public static void init(CommandDispatcher dis) {

        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();
            IntWrapper NUMBER = new IntWrapper("point_amount");
            StringWrapper POINT_TYPE = new StringWrapper("point_type", () -> Arrays.stream(PlayerPointsType.values()).map(e -> e.name()).collect(Collectors.toList()));

            x.addLiteral("points", PermWrapper.OP);
            x.addLiteral("give", PermWrapper.OP);

            x.addArg(PLAYER);
            x.addArg(POINT_TYPE);
            x.addArg(NUMBER);

            x.action(e -> {
                var p = PLAYER.get(e);
                var num = NUMBER.get(e);
                var type = PlayerPointsType.valueOf(POINT_TYPE.get(e));

                var data = Load.player(p).points.get(type);

                var result = data.giveBonusPoints(num);

                if (result.answer != null) {
                    p.sendSystemMessage(result.answer);
                }
            });

        }, "Give player bonus points. The amount you can give is managed by the Game balance datapack. These are separate from points gained per level");

        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();
            StringWrapper POINT_TYPE = new StringWrapper("point_type", () -> Arrays.stream(PlayerPointsType.values()).map(e -> e.name()).collect(Collectors.toList()));

            x.addLiteral("points", PermWrapper.OP);
            x.addLiteral("reset", PermWrapper.OP);

            x.addArg(PLAYER);
            x.addArg(POINT_TYPE);

            x.action(e -> {
                var p = PLAYER.get(e);
                var type = PlayerPointsType.valueOf(POINT_TYPE.get(e));

                var data = Load.player(p).points.get(type);

                data.resetBonusPoints();

                p.sendSystemMessage(Chats.RESET_POINTS.locName(type.word().locName()));
            });

        }, "Resets bonus points of player");


        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();

            x.addLiteral("open", PermWrapper.OP);
            x.addLiteral("hub", PermWrapper.OP);

            x.addArg(PLAYER);

            x.action(e -> {
                var p = PLAYER.get(e);
                Packets.sendToClient(p, new OpenGuiPacket(OpenGuiPacket.GuiType.MAIN_HUB));
            });

        }, "Opens MNS Hub Gui");

        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();
            IntWrapper NUMBER = new IntWrapper("level");

            x.addLiteral("set", PermWrapper.OP);
            x.addLiteral("favor", PermWrapper.OP);

            x.addArg(PLAYER);
            x.addArg(NUMBER);

            x.action(e -> {
                var p = PLAYER.get(e);
                var num = NUMBER.get(e);
                Load.player(p).favor.set(p, num);
            });

        }, "Sets Favor");


        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();
            var PROFESSION = new RegistryWrapper<Profession>(ExileRegistryTypes.PROFESSION);
            IntWrapper NUMBER = new IntWrapper("level");

            x.addLiteral("set", PermWrapper.OP);
            x.addLiteral("profession_level", PermWrapper.OP);

            x.addArg(PLAYER);
            x.addArg(PROFESSION);
            x.addArg(NUMBER);


            x.action(e -> {
                var en = PLAYER.get(e);
                var num = NUMBER.get(e);
                var prof = PROFESSION.get(e);

                PlayerData data = Load.player(en);
                data.professions.setLevel(prof, num);
            });

        }, "Sets Mine and Slash Profession level");

        CommandBuilder.of(dis, x -> {
            PlayerWrapper PLAYER = new PlayerWrapper();
            StringWrapper STRING = new StringWrapper("reset_type", () -> Arrays.stream(ResetPlayerData.values()).map(e -> e.name()).collect(Collectors.toList()));

            x.addLiteral("reset", PermWrapper.OP);
            x.addLiteral("player_data", PermWrapper.OP);

            x.addArg(PLAYER);
            x.addArg(STRING);

            x.action(e -> {
                var p = PLAYER.get(e);
                var s = STRING.get(e);
                ResetPlayerData reset = ResetPlayerData.valueOf(s);
                reset.reset(p);
            });

        }, "Resets parts of a player's data. You can reset their level, talents etc");


    }
}
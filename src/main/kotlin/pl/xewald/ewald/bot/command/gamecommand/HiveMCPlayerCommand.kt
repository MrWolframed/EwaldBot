package pl.xewald.ewald.bot.command.gamecommand

import khttp.get
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import org.json.JSONArray
import org.json.JSONObject
import pl.xewald.ewald.bot.EwaldBot
import pl.xewald.ewald.bot.command.util.Command
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import khttp.delete as httpDelete

class HiveMCPlayerCommand(val bot: EwaldBot) : Command(
        "hivemc",
        "Sprawdź statystyki na serwerze hivemc.com",
        listOf("pomoc")
) {
    override fun execute(member: Member?, channel: MessageChannel, message: Message, args: Array<String>) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        if (args.size == 1) {
            val mojang_api = khttp.get("https://api.mojang.com/users/profiles/minecraft/${args[0]}")
            val mojang_connect: JSONObject = mojang_api.jsonObject
            val uuid = mojang_connect.get("id")
            val api = khttp.get("http://api.hivemc.com/v1/player/$uuid")
            val obj: JSONObject = api.jsonObject
            val eb = EmbedBuilder().apply {
                setAuthor("Statystyki gracza ${args[0]}")
                setColor(member!!.color)
                addField("Nazwa rangi", "${obj.get("rankName")}", true)
                addField("Tokeny", "${obj.get("tokens")}", true)
                addField("Medale", "${obj.get("medals")}", true)
                addField("Punkty", "${obj.get("credits")}", true)
                setThumbnail("https://visage.surgeplay.com/face/$uuid")
                setFooter("EwaldBot, Serwer: hivemc.com $formatted", "https://xewald.pl/Ewald.gif")
            }
            channel.sendMessage(eb.build()).queue()
        } else {
            channel.sendMessage("Prawidłowe użycie: !hivemc <nick>").queue()
        }

    }
}
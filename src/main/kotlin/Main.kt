import com.github.ajalt.clikt.core.subcommands
import ua.com.radiokot.mcc.cli.Mcc
import ua.com.radiokot.mcc.cli.Parse
import ua.com.radiokot.mcc.cli.ParseKinopoisk

fun main(args: Array<String>) = Mcc()
    .subcommands(
        Parse().subcommands(
            ParseKinopoisk()
        )
    )
    .main(args)
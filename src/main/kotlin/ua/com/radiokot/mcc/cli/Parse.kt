package ua.com.radiokot.mcc.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice

class Parse : CliktCommand(
    name = "parse",
    help = "Parses existing movie collection"
) {

    override fun run() = Unit
}
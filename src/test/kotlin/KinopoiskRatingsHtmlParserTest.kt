import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.mcc.kinopoisk.rating.parser.KinopoiskRatingsHtmlParser
import java.io.File

class KinopoiskRatingsHtmlParserTest {
    @Test
    fun parseRatingsHtml() {
        val input = this.javaClass.getResource("/kp_votes_test.html")!!

        val results = KinopoiskRatingsHtmlParser().parse(File(input.file))
        results.forEach(::println)

        val expectedStrings = """
            Дни жатвы Days of Heaven 1978 6.0 10 1614465900000
            Дело Ричарда Джуэлла Richard Jewell 2019 6.0 10 1614545520000
            Земля кочевников Nomadland 2020 7.0 10 1614898140000
            ДМБ ДМБ 2000 7.0 10 1615677780000
            Ненависть La haine 1995 8.0 10 1615841700000
            Карточный домик House of Cards 2013 8.0 10 1616007420000
            Невероятная жизнь Уолтера Митти The Secret Life of Walter Mitty 2013 6.0 10 1616103360000
            Учитель на замену Detachment 2011 8.0 10 1616274120000
            Комплекс Баадер-Майнхоф Der Baader Meinhof Komplex 2008 10.0 10 1616539980000
            Я Кристина Christiane F. - Wir Kinder vom Bahnhof Zoo 1981 8.0 10 1616710980000
            Капернаум Capharnaüm 2018 9.0 10 1616885460000
            Гудини Houdini 2014 7.0 10 1617135960000
            Жить Жить 2010 5.0 10 1617394860000
            Майор Гром: Чумной Доктор Майор Гром: Чумной Доктор 2021 2.0 10 1617992340000
            Дом, который построил Джек The House That Jack Built 2018 5.0 10 1618089240000
            Лиля навсегда Lilja 4-ever 2002 8.0 10 1618166460000
            Generation П Generation П 2011 9.0 10 1618649640000
            Бартон Финк Barton Fink 1991 8.0 10 1618691820000
            Просто кровь Blood Simple 1983 6.0 10 1618775580000
            Никто Nobody 2021 8.0 10 1618863480000
            Тьма Dark 2017 7.0 10 1619725500000
            Чернобыль Chernobyl 2019 10.0 10 1619725740000
            Кояанискатси Koyaanisqatsi 1983 9.0 10 1619896140000
            Таксист Taxi Driver 1976 5.0 10 1620593940000
            Ты You 2018 5.0 10 1622301300000
            Костер тщеславий The Bonfire of the Vanities 1990 9.0 10 1622407020000
            Рыцари справедливости Retfærdighedens ryttere 2020 7.0 10 1623184740000
            Мост Bron/Broen 2011 6.0 10 1624133760000
            Люпен Lupin 2021 6.0 10 1624717380000
            Гнев человеческий Wrath of Man 2021 5.0 10 1624724280000
            Загадочная история Бенджамина Баттона The Curious Case of Benjamin Button 2008 8.0 10 1624741080000
            Till Lindemann: Ich hasse Kinder Till Lindemann: Ich hasse Kinder 2021 7.0 10 1624803360000
            Великий Minamata 2020 6.0 10 1625079180000
            Подпольная империя Boardwalk Empire 2010 8.0 10 1625990940000
            Операция «Шаровая молния» Entebbe 2017 7.0 10 1627150680000
            Куриоса Curiosa 2019 6.0 10 1629147480000
            Властелин колец: Братство Кольца The Lord of the Rings: The Fellowship of the Ring 2001 7.0 10 1630087620000
            Унесённые призраками Sen to Chihiro no kamikakushi 2001 7.0 10 1630087680000
            Игла Игла 1988 6.0 10 1630792380000
            Северные воды The North Water 2021 7.0 10 1631040600000
            Принцесса Мононоке Mononoke-hime 1997 7.0 10 1632647520000
            Не время умирать No Time to Die 2020 7.0 10 1633123080000
            Выживут только любовники Only Lovers Left Alive 2013 8.0 10 1633123080000
            Легенда о волках WolfWalkers 2020 6.0 10 1635012600000
            Французский вестник. Приложение к газете «Либерти. Канзас ивнинг сан» The French Dispatch 2021 7.0 10 1636213140000
            Стена Pink Floyd: The Wall 1982 9.0 10 1637963400000
            Адвокат дьявола The Devil's Advocate 1997 8.0 10 1643067360000
            Укрощение строптивого Il bisbetico domato 1980 5.0 10 1643749320000
            Аллея кошмаров Nightmare Alley 2021 7.0 10 1644686460000
            Пацаны The Boys 2019 8.0 10 1659377700000
        """.trimIndent().lines()

        results.forEachIndexed { i, it ->
            val actual =
                "${it.movie.russianName} ${it.movie.convenientName} ${it.movie.releaseYear} ${it.rating.stars} ${it.rating.of} ${it.date.toEpochMilli()}"
            val expected = expectedStrings[i]
            Assert.assertEquals(expected, actual)
        }
    }
}
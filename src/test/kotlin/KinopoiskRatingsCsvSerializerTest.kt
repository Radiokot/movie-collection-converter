import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsHtmlParser
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsFriendlyCsvSerializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class KinopoiskRatingsCsvSerializerTest {
    private val mapper = CsvMapper().apply { registerKotlinModule() }

    private val expectedCsv = """
                "Movie russian name","Release year","Your rating","Rated at (local time)","Movie page","Movie convenient name"
                "Дни жатвы",1978,6.0,"28.02.2021, 00:45","https://www.kinopoisk.ru/film/18431/","Days of Heaven"
                "Дело Ричарда Джуэлла",2019,6.0,"28.02.2021, 22:52","https://www.kinopoisk.ru/film/826373/","Richard Jewell"
                "Земля кочевников",2020,7.0,"05.03.2021, 00:49","https://www.kinopoisk.ru/film/1238506/",Nomadland
                ДМБ,2000,7.0,"14.03.2021, 01:23","https://www.kinopoisk.ru/film/41133/",ДМБ
                Ненависть,1995,8.0,"15.03.2021, 22:55","https://www.kinopoisk.ru/film/9448/","La haine"
                "Карточный домик",2013,8.0,"17.03.2021, 20:57","https://www.kinopoisk.ru/series/581937/","House of Cards"
                "Невероятная жизнь Уолтера Митти",2013,6.0,"18.03.2021, 23:36","https://www.kinopoisk.ru/film/6034/","The Secret Life of Walter Mitty"
                "Учитель на замену",2011,8.0,"20.03.2021, 23:02","https://www.kinopoisk.ru/film/535337/",Detachment
                "Комплекс Баадер-Майнхоф",2008,10.0,"24.03.2021, 00:53","https://www.kinopoisk.ru/film/279756/","Der Baader Meinhof Komplex"
                "Я Кристина",1981,8.0,"26.03.2021, 00:23","https://www.kinopoisk.ru/film/55830/","Christiane F. - Wir Kinder vom Bahnhof Zoo"
                Капернаум,2018,9.0,"28.03.2021, 00:51","https://www.kinopoisk.ru/film/1118042/",Capharnaüm
                Гудини,2014,7.0,"30.03.2021, 23:26","https://www.kinopoisk.ru/series/818775/",Houdini
                Жить,2010,5.0,"02.04.2021, 23:21","https://www.kinopoisk.ru/film/521526/",Жить
                "Майор Гром: Чумной Доктор",2021,2.0,"09.04.2021, 21:19","https://www.kinopoisk.ru/film/1109271/","Майор Гром: Чумной Доктор"
                "Дом, который построил Джек",2018,5.0,"11.04.2021, 00:14","https://www.kinopoisk.ru/film/942396/","The House That Jack Built"
                "Лиля навсегда",2002,8.0,"11.04.2021, 21:41","https://www.kinopoisk.ru/film/739/","Lilja 4-ever"
                "Generation П",2011,9.0,"17.04.2021, 11:54","https://www.kinopoisk.ru/film/104893/","Generation П"
                "Бартон Финк",1991,8.0,"17.04.2021, 23:37","https://www.kinopoisk.ru/film/2890/","Barton Fink"
                "Просто кровь",1983,6.0,"18.04.2021, 22:53","https://www.kinopoisk.ru/film/2803/","Blood Simple"
                Никто,2021,8.0,"19.04.2021, 23:18","https://www.kinopoisk.ru/film/1309596/",Nobody
                Тьма,2017,7.0,"29.04.2021, 22:45","https://www.kinopoisk.ru/series/1032606/",Dark
                Чернобыль,2019,10.0,"29.04.2021, 22:49","https://www.kinopoisk.ru/series/1227803/",Chernobyl
                Кояанискатси,1983,9.0,"01.05.2021, 22:09","https://www.kinopoisk.ru/film/7328/",Koyaanisqatsi
                Таксист,1976,5.0,"09.05.2021, 23:59","https://www.kinopoisk.ru/film/358/","Taxi Driver"
                Ты,2018,5.0,"29.05.2021, 18:15","https://www.kinopoisk.ru/series/1115630/",You
                "Костер тщеславий",1990,9.0,"30.05.2021, 23:37","https://www.kinopoisk.ru/film/6065/","The Bonfire of the Vanities"
                "Рыцари справедливости",2020,7.0,"08.06.2021, 23:39","https://www.kinopoisk.ru/film/1345158/","Retfærdighedens ryttere"
                Мост,2011,6.0,"19.06.2021, 23:16","https://www.kinopoisk.ru/series/574497/",Bron/Broen
                Люпен,2021,6.0,"26.06.2021, 17:23","https://www.kinopoisk.ru/series/719762/",Lupin
                "Гнев человеческий",2021,5.0,"26.06.2021, 19:18","https://www.kinopoisk.ru/film/1318972/","Wrath of Man"
                "Загадочная история Бенджамина Баттона",2008,8.0,"26.06.2021, 23:58","https://www.kinopoisk.ru/film/81555/","The Curious Case of Benjamin Button"
                "Till Lindemann: Ich hasse Kinder",2021,7.0,"27.06.2021, 17:16","https://www.kinopoisk.ru/film/4473744/","Till Lindemann: Ich hasse Kinder"
                Великий,2020,6.0,"30.06.2021, 21:53","https://www.kinopoisk.ru/film/1208173/",Minamata
                "Подпольная империя",2010,8.0,"11.07.2021, 11:09","https://www.kinopoisk.ru/series/474779/","Boardwalk Empire"
                "Операция «Шаровая молния»",2017,7.0,"24.07.2021, 21:18","https://www.kinopoisk.ru/film/995013/",Entebbe
                Куриоса,2019,6.0,"16.08.2021, 23:58","https://www.kinopoisk.ru/film/1098909/",Curiosa
                "Властелин колец: Братство Кольца",2001,7.0,"27.08.2021, 21:07","https://www.kinopoisk.ru/film/328/","The Lord of the Rings: The Fellowship of the Ring"
                "Унесённые призраками",2001,7.0,"27.08.2021, 21:08","https://www.kinopoisk.ru/film/370/","Sen to Chihiro no kamikakushi"
                Игла,1988,6.0,"05.09.2021, 00:53","https://www.kinopoisk.ru/film/42576/",Игла
                "Северные воды",2021,7.0,"07.09.2021, 21:50","https://www.kinopoisk.ru/series/1263067/","The North Water"
                "Принцесса Мононоке",1997,7.0,"26.09.2021, 12:12","https://www.kinopoisk.ru/film/441/",Mononoke-hime
                "Не время умирать",2020,7.0,"02.10.2021, 00:18","https://www.kinopoisk.ru/film/706019/","No Time to Die"
                "Выживут только любовники",2013,8.0,"02.10.2021, 00:18","https://www.kinopoisk.ru/film/565819/","Only Lovers Left Alive"
                "Легенда о волках",2020,6.0,"23.10.2021, 21:10","https://www.kinopoisk.ru/film/958541/",WolfWalkers
                "Французский вестник. Приложение к газете «Либерти. Канзас ивнинг сан»",2021,7.0,"06.11.2021, 17:39","https://www.kinopoisk.ru/film/1211076/","The French Dispatch"
                Стена,1982,9.0,"26.11.2021, 23:50","https://www.kinopoisk.ru/film/23712/","Pink Floyd: The Wall"
                "Адвокат дьявола",1997,8.0,"25.01.2022, 01:36","https://www.kinopoisk.ru/film/3797/","The Devil's Advocate"
                "Укрощение строптивого",1980,5.0,"01.02.2022, 23:02","https://www.kinopoisk.ru/film/63912/","Il bisbetico domato"
                "Аллея кошмаров",2021,7.0,"12.02.2022, 19:21","https://www.kinopoisk.ru/film/1125892/","Nightmare Alley"
                Пацаны,2019,8.0,"01.08.2022, 21:15","https://www.kinopoisk.ru/series/460586/","The Boys"
                
            """.trimIndent()

    @Test
    fun write() {
        val kpInput = this.javaClass.getResource("/kp_votes_test.html")!!

        val kpRatings = KinopoiskRatingsHtmlParser()
            .parse(File(kpInput.file))

        val output = ByteArrayOutputStream()

        KinopoiskRatingsFriendlyCsvSerializer(mapper)
            .write(kpRatings, output)

        Assert.assertEquals(
            expectedCsv,
            output.toByteArray().decodeToString()
        )
    }

    @Test
    fun read() {
        val kpInput = this.javaClass.getResource("/kp_votes_test.html")!!

        val kpRatings = KinopoiskRatingsHtmlParser()
            .parse(File(kpInput.file))

        val readRatings = KinopoiskRatingsFriendlyCsvSerializer(mapper)
            .read(ByteArrayInputStream(expectedCsv.toByteArray()))

        Assert.assertArrayEquals(
            kpRatings.toTypedArray(),
            readRatings.toTypedArray()
        )
    }
}
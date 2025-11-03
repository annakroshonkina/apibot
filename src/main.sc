theme: /

require: functions.js

state: Start
    q!: /<start>/
    a: Привет! Я электронный помощник. Я могу сообщить вам текущую погоду в любом городе. Напишите город.

state: GetWeather
    intent!: /geo/
    script:
        var city = $caila.inflect($parseTree._geo, ["nomn"]);
        openWeatherMapCurrent("metric", "ru", city).then(function (res) {
            if (res && res.weather && res.main && res.name) {
                var description = res.weather[0].description; // Например: "небольшой дождь"
                var mainWeather = res.weather[0].main; // Например: "Rain"
                var temp = Math.round(res.main.temp); // Температура
                var cityName = res.name; // Название из API

                var replyMsg = "Сегодня в городе " + capitalize(cityName) + " " + description + ", " + temp + "°C";

                if (mainWeather == 'Rain' || mainWeather == 'Drizzle') {
                    replyMsg += ". Советую захватить с собой зонтик!";
                } else if (Math.round(res.main.temp) < 0) {
                    replyMsg += ". Бррррр ну и мороз";
                }

                $reactions.answer(replyMsg);
            } else {
                $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
            }
        }).catch(function (err) {
            $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
        });

state: CatchAll
    event!: noMatch
    a: Извините, я вас не понимаю, зато могу рассказать о погоде. Введите название города
    go: /GetWeather
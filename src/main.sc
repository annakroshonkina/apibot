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
                var cityName = res.name; // Название города
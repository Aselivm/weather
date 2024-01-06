package org.primshic.stepan.test_cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.services.WeatherAPIService;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TestAPI {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private WeatherAPIService weatherAPIService;

    @Test
    void locationList(){
        List<LocationDTO> locationDTOList1;
        final String LONDON = "London";
        String answer = "[{\"name\":\"London\",\"local_names\":{\"tk\":\"London\",\"uk\":\"Лондон\",\"hy\":\"Լոնդոն\",\"ka\":\"ლონდონი\",\"ha\":\"Landan\",\"no\":\"London\",\"sh\":\"London\",\"cv\":\"Лондон\",\"tl\":\"Londres\",\"ay\":\"London\",\"bg\":\"Лондон\",\"ht\":\"Lonn\",\"na\":\"London\",\"gv\":\"Lunnin\",\"km\":\"ឡុងដ៍\",\"ru\":\"Лондон\",\"am\":\"ለንደን\",\"gu\":\"લંડન\",\"ie\":\"London\",\"vi\":\"Luân Đôn\",\"ln\":\"Lóndɛlɛ\",\"yi\":\"לאנדאן\",\"si\":\"ලන්ඩන්\",\"tr\":\"Londra\",\"mr\":\"लंडन\",\"gl\":\"Londres\",\"sr\":\"Лондон\",\"gd\":\"Lunnainn\",\"io\":\"London\",\"id\":\"London\",\"ig\":\"London\",\"feature_name\":\"London\",\"lt\":\"Londonas\",\"da\":\"London\",\"jv\":\"London\",\"ne\":\"लन्डन\",\"mk\":\"Лондон\",\"el\":\"Λονδίνο\",\"nv\":\"Tooh Dineʼé Bikin Haalʼá\",\"kn\":\"ಲಂಡನ್\",\"mn\":\"Лондон\",\"fr\":\"Londres\",\"se\":\"London\",\"ja\":\"ロンドン\",\"gn\":\"Lóndyre\",\"su\":\"London\",\"kk\":\"Лондон\",\"bs\":\"London\",\"to\":\"Lonitoni\",\"oc\":\"Londres\",\"sl\":\"London\",\"hr\":\"London\",\"bi\":\"London\",\"ia\":\"London\",\"ascii\":\"London\",\"ko\":\"런던\",\"nn\":\"London\",\"sa\":\"लन्डन्\",\"ps\":\"لندن\",\"tt\":\"Лондон\",\"ku\":\"London\",\"sc\":\"Londra\",\"ml\":\"ലണ്ടൻ\",\"bo\":\"ལོན་ཊོན།\",\"li\":\"Londe\",\"nl\":\"Londen\",\"lv\":\"Londona\",\"ca\":\"Londres\",\"af\":\"Londen\",\"uz\":\"London\",\"th\":\"ลอนดอน\",\"he\":\"לונדון\",\"ky\":\"Лондон\",\"it\":\"Londra\",\"te\":\"లండన్\",\"co\":\"Londra\",\"br\":\"Londrez\",\"az\":\"London\",\"kl\":\"London\",\"ga\":\"Londain\",\"ab\":\"Лондон\",\"ta\":\"இலண்டன்\",\"bn\":\"লন্ডন\",\"so\":\"London\",\"fa\":\"لندن\",\"be\":\"Лондан\",\"mt\":\"Londra\",\"vo\":\"London\",\"st\":\"London\",\"or\":\"ଲଣ୍ଡନ\",\"av\":\"Лондон\",\"ba\":\"Лондон\",\"fo\":\"London\",\"ar\":\"لندن\",\"de\":\"London\",\"fi\":\"Lontoo\",\"lb\":\"London\",\"wa\":\"Londe\",\"cs\":\"Londýn\",\"os\":\"Лондон\",\"ro\":\"Londra\",\"ur\":\"علاقہ لندن\",\"fy\":\"Londen\",\"ms\":\"London\",\"pt\":\"Londres\",\"kv\":\"Лондон\",\"pl\":\"Londyn\",\"cu\":\"Лондонъ\",\"ce\":\"Лондон\",\"eo\":\"Londono\",\"is\":\"London\",\"yo\":\"Lọndọnu\",\"kw\":\"Loundres\",\"es\":\"Londres\",\"hi\":\"लंदन\",\"wo\":\"Londar\",\"sk\":\"Londýn\",\"sd\":\"لنڊن\",\"sm\":\"Lonetona\",\"sn\":\"London\",\"lo\":\"ລອນດອນ\",\"om\":\"Landan\",\"sv\":\"London\",\"rm\":\"Londra\",\"zh\":\"伦敦\",\"en\":\"London\",\"bh\":\"लंदन\",\"fj\":\"Lodoni\",\"mg\":\"Lôndôna\",\"cy\":\"Llundain\",\"tg\":\"Лондон\",\"my\":\"လန်ဒန်မြို့\",\"hu\":\"London\",\"ny\":\"London\",\"sq\":\"Londra\",\"tw\":\"London\",\"an\":\"Londres\",\"eu\":\"Londres\",\"ug\":\"لوندۇن\",\"sw\":\"London\",\"bm\":\"London\",\"ff\":\"London\",\"qu\":\"London\",\"zu\":\"ILondon\",\"ee\":\"London\",\"et\":\"London\",\"pa\":\"ਲੰਡਨ\",\"mi\":\"Rānana\"},\"lat\":51.5073219,\"lon\":-0.1276474,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"City of London\",\"local_names\":{\"fr\":\"Cité de Londres\",\"ru\":\"Сити\",\"ur\":\"لندن شہر\",\"uk\":\"Лондонське Сіті\",\"lt\":\"Londono Sitis\",\"he\":\"הסיטי של לונדון\",\"zh\":\"倫敦市\",\"en\":\"City of London\",\"pt\":\"Cidade de Londres\",\"hi\":\"सिटी ऑफ़ लंदन\",\"ko\":\"시티 오브 런던\",\"es\":\"City de Londres\"},\"lat\":51.5156177,\"lon\":-0.0919983,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"local_names\":{\"ko\":\"런던\",\"hy\":\"Լոնտոն\",\"ga\":\"Londain\",\"ug\":\"لوندۇن\",\"lv\":\"Landona\",\"ar\":\"لندن\",\"lt\":\"Londonas\",\"ka\":\"ლონდონი\",\"yi\":\"לאנדאן\",\"oj\":\"Baketigweyaang\",\"bn\":\"লন্ডন\",\"ja\":\"ロンドン\",\"he\":\"לונדון\",\"en\":\"London\",\"be\":\"Лондан\",\"th\":\"ลอนดอน\",\"ru\":\"Лондон\",\"fr\":\"London\",\"cr\":\"ᓬᐊᐣᑕᐣ\",\"fa\":\"لندن\",\"iu\":\"ᓚᓐᑕᓐ\",\"el\":\"Λόντον\"},\"lat\":42.9832406,\"lon\":-81.243372,\"country\":\"CA\",\"state\":\"Ontario\"},{\"name\":\"Chelsea\",\"local_names\":{\"et\":\"Chelsea\",\"ru\":\"Челси\",\"sv\":\"Chelsea, London\",\"hu\":\"Chelsea\",\"da\":\"Chelsea\",\"az\":\"Çelsi\",\"en\":\"Chelsea\",\"fr\":\"Chelsea\",\"pl\":\"Chelsea\",\"es\":\"Chelsea\",\"fa\":\"چلسی\",\"ar\":\"تشيلسي\",\"af\":\"Chelsea, Londen\",\"ur\":\"چیلسی، لندن\",\"zh\":\"車路士\",\"hi\":\"चेल्सी, लंदन\",\"pt\":\"Chelsea\",\"eu\":\"Chelsea\",\"nl\":\"Chelsea\",\"ko\":\"첼시\",\"sh\":\"Chelsea, London\",\"ja\":\"チェルシー\",\"de\":\"Chelsea\",\"ga\":\"Chelsea\",\"sk\":\"Chelsea\",\"no\":\"Chelsea\",\"tr\":\"Chelsea, Londra\",\"uk\":\"Челсі\",\"he\":\"צ'לסי\",\"it\":\"Chelsea\",\"vi\":\"Chelsea, Luân Đôn\",\"id\":\"Chelsea, London\",\"el\":\"Τσέλσι\"},\"lat\":51.4875167,\"lon\":-0.1687007,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"lat\":37.1289771,\"lon\":-84.0832646,\"country\":\"US\",\"state\":\"Kentucky\"}]";
        try {
            locationDTOList1 = objectMapper.readValue(answer, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertThat(locationDTOList1).hasSize(5);
        Mockito.when(weatherAPIService.getLocationListByName(LONDON)).thenReturn(locationDTOList1);
        List<LocationDTO> locationDTOList2 = weatherAPIService.getLocationListByName(LONDON);
        assertThat(locationDTOList2).containsExactlyInAnyOrderElementsOf(locationDTOList1);
    }

    @Test
    void locationWeather(){
        LocationWeatherDTO locationWeatherDTO1;
        final BigDecimal lat = new BigDecimal(37.1289771);
        final BigDecimal lon = new BigDecimal(-84.0832646);
        final Location location = new Location();
        location.setLat(lat);
        location.setLon(lon);
        String answer = "{\"coord\":{\"lon\":-84.0833,\"lat\":37.129},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":279.41,\"feels_like\":278.62,\"temp_min\":279.02,\"temp_max\":280.51,\"pressure\":1020,\"humidity\":52,\"sea_level\":1020,\"grnd_level\":974},\"visibility\":10000,\"wind\":{\"speed\":1.43,\"deg\":111,\"gust\":1.37},\"clouds\":{\"all\":97},\"dt\":1704491746,\"sys\":{\"type\":2,\"id\":2009370,\"country\":\"US\",\"sunrise\":1704459016,\"sunset\":1704493946},\"timezone\":-18000,\"id\":4298960,\"name\":\"London\",\"cod\":200}";

        try {
            locationWeatherDTO1 = objectMapper.readValue(answer, LocationWeatherDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(weatherAPIService.getWeatherByLocation(location)).thenReturn(locationWeatherDTO1);

        LocationWeatherDTO locationWeatherDTO2 = weatherAPIService.getWeatherByLocation(location);
        assertThat(locationWeatherDTO1!=null);
        assertThat(locationWeatherDTO1).isEqualTo(locationWeatherDTO2);
    }

    @Test
    void containsExpectedLocation() {
        final BigDecimal lat = new BigDecimal(37.1289771);
        final BigDecimal lon = new BigDecimal(-84.0832646);
        final Location loc = new Location();
        loc.setLat(lat);
        loc.setLon(lon);

        List<LocationDTO> locationDTOList1;
        final String LONDON = "London";
        String answer = "[{\"name\":\"London\",\"local_names\":{\"tk\":\"London\",\"uk\":\"Лондон\",\"hy\":\"Լոնդոն\",\"ka\":\"ლონდონი\",\"ha\":\"Landan\",\"no\":\"London\",\"sh\":\"London\",\"cv\":\"Лондон\",\"tl\":\"Londres\",\"ay\":\"London\",\"bg\":\"Лондон\",\"ht\":\"Lonn\",\"na\":\"London\",\"gv\":\"Lunnin\",\"km\":\"ឡុងដ៍\",\"ru\":\"Лондон\",\"am\":\"ለንደን\",\"gu\":\"લંડન\",\"ie\":\"London\",\"vi\":\"Luân Đôn\",\"ln\":\"Lóndɛlɛ\",\"yi\":\"לאנדאן\",\"si\":\"ලන්ඩන්\",\"tr\":\"Londra\",\"mr\":\"लंडन\",\"gl\":\"Londres\",\"sr\":\"Лондон\",\"gd\":\"Lunnainn\",\"io\":\"London\",\"id\":\"London\",\"ig\":\"London\",\"feature_name\":\"London\",\"lt\":\"Londonas\",\"da\":\"London\",\"jv\":\"London\",\"ne\":\"लन्डन\",\"mk\":\"Лондон\",\"el\":\"Λονδίνο\",\"nv\":\"Tooh Dineʼé Bikin Haalʼá\",\"kn\":\"ಲಂಡನ್\",\"mn\":\"Лондон\",\"fr\":\"Londres\",\"se\":\"London\",\"ja\":\"ロンドン\",\"gn\":\"Lóndyre\",\"su\":\"London\",\"kk\":\"Лондон\",\"bs\":\"London\",\"to\":\"Lonitoni\",\"oc\":\"Londres\",\"sl\":\"London\",\"hr\":\"London\",\"bi\":\"London\",\"ia\":\"London\",\"ascii\":\"London\",\"ko\":\"런던\",\"nn\":\"London\",\"sa\":\"लन्डन्\",\"ps\":\"لندن\",\"tt\":\"Лондон\",\"ku\":\"London\",\"sc\":\"Londra\",\"ml\":\"ലണ്ടൻ\",\"bo\":\"ལོན་ཊོན།\",\"li\":\"Londe\",\"nl\":\"Londen\",\"lv\":\"Londona\",\"ca\":\"Londres\",\"af\":\"Londen\",\"uz\":\"London\",\"th\":\"ลอนดอน\",\"he\":\"לונדון\",\"ky\":\"Лондон\",\"it\":\"Londra\",\"te\":\"లండన్\",\"co\":\"Londra\",\"br\":\"Londrez\",\"az\":\"London\",\"kl\":\"London\",\"ga\":\"Londain\",\"ab\":\"Лондон\",\"ta\":\"இலண்டன்\",\"bn\":\"লন্ডন\",\"so\":\"London\",\"fa\":\"لندن\",\"be\":\"Лондан\",\"mt\":\"Londra\",\"vo\":\"London\",\"st\":\"London\",\"or\":\"ଲଣ୍ଡନ\",\"av\":\"Лондон\",\"ba\":\"Лондон\",\"fo\":\"London\",\"ar\":\"لندن\",\"de\":\"London\",\"fi\":\"Lontoo\",\"lb\":\"London\",\"wa\":\"Londe\",\"cs\":\"Londýn\",\"os\":\"Лондон\",\"ro\":\"Londra\",\"ur\":\"علاقہ لندن\",\"fy\":\"Londen\",\"ms\":\"London\",\"pt\":\"Londres\",\"kv\":\"Лондон\",\"pl\":\"Londyn\",\"cu\":\"Лондонъ\",\"ce\":\"Лондон\",\"eo\":\"Londono\",\"is\":\"London\",\"yo\":\"Lọndọnu\",\"kw\":\"Loundres\",\"es\":\"Londres\",\"hi\":\"लंदन\",\"wo\":\"Londar\",\"sk\":\"Londýn\",\"sd\":\"لنڊن\",\"sm\":\"Lonetona\",\"sn\":\"London\",\"lo\":\"ລອນດອນ\",\"om\":\"Landan\",\"sv\":\"London\",\"rm\":\"Londra\",\"zh\":\"伦敦\",\"en\":\"London\",\"bh\":\"लंदन\",\"fj\":\"Lodoni\",\"mg\":\"Lôndôna\",\"cy\":\"Llundain\",\"tg\":\"Лондон\",\"my\":\"လန်ဒန်မြို့\",\"hu\":\"London\",\"ny\":\"London\",\"sq\":\"Londra\",\"tw\":\"London\",\"an\":\"Londres\",\"eu\":\"Londres\",\"ug\":\"لوندۇن\",\"sw\":\"London\",\"bm\":\"London\",\"ff\":\"London\",\"qu\":\"London\",\"zu\":\"ILondon\",\"ee\":\"London\",\"et\":\"London\",\"pa\":\"ਲੰਡਨ\",\"mi\":\"Rānana\"},\"lat\":51.5073219,\"lon\":-0.1276474,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"City of London\",\"local_names\":{\"fr\":\"Cité de Londres\",\"ru\":\"Сити\",\"ur\":\"لندن شہر\",\"uk\":\"Лондонське Сіті\",\"lt\":\"Londono Sitis\",\"he\":\"הסיטי של לונדון\",\"zh\":\"倫敦市\",\"en\":\"City of London\",\"pt\":\"Cidade de Londres\",\"hi\":\"सिटी ऑफ़ लंदन\",\"ko\":\"시티 오브 런던\",\"es\":\"City de Londres\"},\"lat\":51.5156177,\"lon\":-0.0919983,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"local_names\":{\"ko\":\"런던\",\"hy\":\"Լոնտոն\",\"ga\":\"Londain\",\"ug\":\"لوندۇن\",\"lv\":\"Landona\",\"ar\":\"لندن\",\"lt\":\"Londonas\",\"ka\":\"ლონდონი\",\"yi\":\"לאנדאן\",\"oj\":\"Baketigweyaang\",\"bn\":\"লন্ডন\",\"ja\":\"ロンドン\",\"he\":\"לונדון\",\"en\":\"London\",\"be\":\"Лондан\",\"th\":\"ลอนดอน\",\"ru\":\"Лондон\",\"fr\":\"London\",\"cr\":\"ᓬᐊᐣᑕᐣ\",\"fa\":\"لندن\",\"iu\":\"ᓚᓐᑕᓐ\",\"el\":\"Λόντον\"},\"lat\":42.9832406,\"lon\":-81.243372,\"country\":\"CA\",\"state\":\"Ontario\"},{\"name\":\"Chelsea\",\"local_names\":{\"et\":\"Chelsea\",\"ru\":\"Челси\",\"sv\":\"Chelsea, London\",\"hu\":\"Chelsea\",\"da\":\"Chelsea\",\"az\":\"Çelsi\",\"en\":\"Chelsea\",\"fr\":\"Chelsea\",\"pl\":\"Chelsea\",\"es\":\"Chelsea\",\"fa\":\"چلسی\",\"ar\":\"تشيلسي\",\"af\":\"Chelsea, Londen\",\"ur\":\"چیلسی، لندن\",\"zh\":\"車路士\",\"hi\":\"चेल्सी, लंदन\",\"pt\":\"Chelsea\",\"eu\":\"Chelsea\",\"nl\":\"Chelsea\",\"ko\":\"첼시\",\"sh\":\"Chelsea, London\",\"ja\":\"チェルシー\",\"de\":\"Chelsea\",\"ga\":\"Chelsea\",\"sk\":\"Chelsea\",\"no\":\"Chelsea\",\"tr\":\"Chelsea, Londra\",\"uk\":\"Челсі\",\"he\":\"צ'לסי\",\"it\":\"Chelsea\",\"vi\":\"Chelsea, Luân Đôn\",\"id\":\"Chelsea, London\",\"el\":\"Τσέλσι\"},\"lat\":51.4875167,\"lon\":-0.1687007,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"lat\":37.1289771,\"lon\":-84.0832646,\"country\":\"US\",\"state\":\"Kentucky\"}]";
        try {
            locationDTOList1 = objectMapper.readValue(answer, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(weatherAPIService.getLocationListByName(LONDON)).thenReturn(locationDTOList1);

        BigDecimal epsilon = BigDecimal.valueOf(0.000001);

        assertThat(weatherAPIService.getLocationListByName(LONDON)).anyMatch(location ->
                location.getLat() - lat.doubleValue() <= epsilon.doubleValue() &&
                        location.getLon() - lon.doubleValue() <= epsilon.doubleValue()
        );
    }

    //todo В случай ошибки (статусы 4xx, 5xx) от OpenWeather API сервис выбрасывает ожидаемый тип исключения
}

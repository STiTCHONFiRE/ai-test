package ru.stitchonfire.aitest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.stitchonfire.aitest.dto.TestRequestDto;
import ru.stitchonfire.aitest.dto.TestResponseDto;
import ru.stitchonfire.aitest.repository.AnnouncementRepository;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestService {

    ChatClient chatClient;
    AnnouncementRepository announcementRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void testRequest() {
        var result = announcementRepository.findByCategory_IdInAndIsDeletedFalseAndStatus_Id(
                List.of(87, 88, 90, 91, 104, 120, 121, 122, 123, 124, 125, 126, 127, 133),
                (short) 2
        );

        Thread.ofVirtual().start(() -> {
            result.stream().limit(5).forEach(announcement -> {
                var res = getTestResponse(new TestRequestDto(announcement.getTitle(), announcement.getDescription()));
                System.out.println("AI Response: " + res.toString() + "\n" + "name: " + announcement.getTitle());
                try {
                    Thread.sleep(Duration.ofSeconds(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public TestResponseDto getTestResponse(TestRequestDto dto) {
        String raw = chatClient.prompt("""
                        Ты модератор онлайн-магазина. Пользователь, похоже, плохо заполняет название товара, нужно привести его в порядок!
                        Проанализируй название и описание объявления, извлеки из них все ключевые данные о товаре и верни их в лаконичном формате:
                        Необходимо строго следовать следующим правилам:
                        В названии должны быть указаны:
                        - тип или категория товара;
                        - производитель или бренд, торговая марка;
                        - модель товара или артикул модели;
                        - важные параметры (например, размер, объем, цвет, количество штук в упаковке и т. д.).
                        
                        Составляйте название по схеме: тип + бренд или производитель + модель + особенности, если есть (например, цвет, размер или вес) и количество в упаковке.
                        
                        Не включайте в название условия продажи (например, «скидка», «бесплатная доставка» и т. д.), эмоциональные характеристики («хит», «супер» и т. д.). Не пишите слова большими буквами — кроме устоявшихся названий брендов и моделей.
                        Не стоит указывать в названии город или регион, в котором находится товар, а также контактные данные продавца.
                        
                        Приведу небольшие примеры:
                        1. Указывайте производителя и тип товара.
                        Неправильно:
                        ```
                        Компьютерное кресло Поло черное
                        ```
                        или
                        ```
                        LEVENHUK LabZZ M101
                        ```
                        Правильно:
                        ```
                        Компьютерное кресло AMF Поло черное
                        ```
                        или
                        ```
                        Микроскоп LEVENHUK LabZZ M101
                        ```
                        2. Указывайте название и/или артикул модели.
                        Неправильно:
                        ```
                        Кухонные весы
                        ```
                        Правильно:
                        ```
                        Кухонные весы BC5000 Optiss
                        ```
                        
                        Оптимальная длина — 50‑60 символов, максимальная — 150.
                        
                        Не рекомендуется использовать в названии:
                        - Использовать слова «новинка», «хит», «бесплатная доставка» и т. п., а также слова, набранные заглавными буквами (кроме устоявшихся названий брендов, моделей товаров).
                        - Использовать HTML-теги.
                        - Указывать информацию о скидках, акциях.
                        
                        Неправильно:
                        ```
                        Видеокамера ST-712 IP PRO D (+ скидка 10 процентов при заказе от 2 шт.)
                        ```
                        Правильно:
                        ```
                        Видеокамера ST-712 IP PRO D
                        ```
                        Указывать название магазина.
                        Неправильно:
                        ```
                        Шкаф купе Концепт 35 МебельМС
                        ```
                        Правильно:
                        ```
                        Шкаф купе Концепт 35
                        ```
                        Указывать информацию о стране производителя товара.
                        Неправильно:
                        ```
                        Входная металлическая дверь Китай Стройгост 5-1 металл 880*2060мм
                        ```
                        Правильно:
                        ```
                        Входная металлическая дверь Стройгост 5-1 металл 880*2060мм
                        ```
                        
                        Если в описании или названии объявления есть несколько артикулов или моделей обязательно укажи только один из них в названии, который наиболее точно описывает товар.
                        
                        В конечном итоге тебе нужно вернуть ТОЛЬКО ОБЪЕКТ В ФОРМАТЕ **JSON**, содержащий одно единственное поле "name", в котором будет название товара, составленное по правилам выше.:
                        К примеру, для товара "Компьютерное кресло AMF Поло черное" нужно вернуть:
                        ```json
                        {
                            "name": "Компьютерное кресло AMF Поло черное"
                        }
                        ```
                        
                        Пример 1:
                        name: Продам телефон Samsung.
                        description: Телефон в хорошем состоянии, модель Galaxy S21, цвет черный.
                        JSON Response:
                        ```json
                        {
                            "name": "Телефон Samsung Galaxy S21 черный"
                        }
                        ```
                        
                        Пример 2:
                        name: Продаю ноутбук Dell XPS 13.
                        description: Новый ноутбук, модель XPS 13, цвет серебристый.
                        JSON Response:
                        ```json
                        {
                            "name": "Ноутбук Dell XPS 13 серебристый"
                        }
                        ```
                        \s
                        Пример 3:
                        name: Продаю видеокарту RXT 3070.
                        description: Продам новую видеокарту NVIDIA RTX 3070 8GB.
                        JSON Response:
                        ```json
                        {
                            "name": "Видеокарта NVIDIA RTX 3070 8GB"
                        }
                        ```
                        
                        Пример 4:
                        name: Датчик давления Komatsu 7861-92-1610.
                        description: "Датчик давления 7861-92-1610 Komatsu: BA100-1, BR200S-1, BR200S-1, BR350JG-1, BR350JG-1, BR550JG-1, BR550JG-1, BRIOOJG-l, BRIOOJ-l, BRIOORG-l, BRIOOR-l, BZ210-1, BZ210-1, D155AX-5, D155AX-5, D475A-3, D475A-3, D475A-3-SC, GC380F-2, PC100-6, PC100-6, PC100-6, PC100-6E, PC100-6S, PC100L-6, PC100L-6, PC100L-6, PC100N-6, PC120-6, PC120-6, PC120-6E, PC120-6E0, PC120-6E0-T2, PC120-6H, PC120-6J, PC120-6S, PC120-6Z, PC120LC-6, PC120LC-6E0, PC120SC-6, PC128US-2, PC128US-2, PC128US-2, PC128UU-1, PC130-6, PC130-6, PC130-6, PC130-6E, PC130-6E0-T2, PC130-6G, PC130-6K, PC138US-2, PC150-6K, PC150LC-6K, PC150LGP-6K, PC158US-2, PC158US-2, PC158USLC-2, PC160-6K, PC1800-6, PC180LC-6K, PC180NLC-6K, PC200-6, PC200-6, PC200-6, PC200-6, PC200-6B, PC200-6H, PC200-6H, PC200-6J, PC200-6LC, PC200-6S, PC200-6S, PC200EL-6K, PC200EN-6K, PC200LC-6, PC200LC-6, PC200LC-6B."
                        ```json
                        {
                            "name": "Датчик давления Komatsu 7861-92-1610"
                        }
                        ```
                        
                        Пример 5:
                        name: Датчик температуры впускного тракта 4651943 Hitach;
                        description: "Продаем Датчик температуры впускного тракта 4651943
                        
                                      Каталожные номера:
                        
                                      4651943
                                      8121468300 8-12146830-0
                                      87378549
                                      263G2-43151
                        
                                      Применяемость:
                        
                                      Hitachi ZX110-3, ZX110M-3, ZX120-3, ZX130-3, ZX130K-3, ZX130LCN-3, ZX140W-3, ZX160LC-3, ZX170W-3, ZX180LC-3, ZX190W-3, ZX200-3, ZX200LC-3, ZX210H-3, ZX210K-3, ZX210LCH-3, ZX210LCK-3, ZX210LCN-3, ZX210W-3, ZX240-3, ZX240LC-3, ZX240N-3, ZX250H-3, ZX250K-3, ZX250LC-3, ZX250LCH-3, ZX250LCK-3, ZX250LCN-3, ZX270-3, ZX270LC-3, ZX280LC-3, ZX280LCH-3, ZX280LCN-3, ZX330-3, ZX330LC-3, ZX350H-3, ZX350K-3, ZX350LC-3, ZX350LCH-3, ZX350LCK-3, ZX350LCN-3, ZX450-3, ZX450LC-3, ZX470H-3, ZX470LCH-3, ZX470LCR-3, ZX470R-3, ZX520LC-3, ZX520LCH-3, ZX520LCR-3, ZX650LC-3, ZX670LCH-3, ZX670LCR-3, ZX850-3, ZX850LC-3, ZX870H-3, ZX870LCH-3, ZX870LCR-3, ZX870R-3
                        
                                      В наличии на складе."
                        ```json
                        {
                            "name": "Датчик температуры впускного тракта Hitachi 4651943"
                        }
                        ```
                        
                        Данные для разбора, всегда возвращай объектом JSON, содержащим только одно поле "name", в котором будет название товара, составленное по правилам выше:
                        name: %s
                        description: %s
                        \s""".formatted(dto.name(), dto.description()))
                .call()
                .content();

        System.out.println("Raw response: " + raw);

        try {
            assert raw != null;
            return extractJson(raw, TestResponseDto.class, new ObjectMapper());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T extractJson(String raw, Class<T> clazz, ObjectMapper mapper) throws IOException {
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start == -1 || end == -1 || start >= end) {
            throw new IOException("Valid JSON object not found in LLM output");
        }
        String json = raw.substring(start, end + 1);
        return mapper.readValue(json, clazz);
    }
}

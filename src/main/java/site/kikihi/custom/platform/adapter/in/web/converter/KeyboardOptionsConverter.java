package site.kikihi.custom.platform.adapter.in.web.converter;

import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeyboardOptionsConverter {

    // Size enum → description 검색 키워드 맵핑
    public static String mapSizeToDescription(KeyboardOptions.Size size) {
        if (size == null) return null;
        return switch (size) {
            case TENKEYLESS -> "텐키리스";
            case FULL -> "풀배열";
            case MINI -> "미니";
        };
    }

    // SwitchType enum → options.option_name 중 매칭되는 문자열 배열 리턴
    public static List<String> mapSwitchTypeToOptionNames(KeyboardOptions.SwitchType switchType) {
        if (switchType == null) return Collections.emptyList();

        return switch (switchType) {
            case SILENT -> Arrays.asList(
                    "저소음 적축", "저소음 갈축", "저소음 흑축",
                    "저소음 바다축", "저소음 잉크축", "저소음 바닐라축",
                    "저소음 딸기축", "저소음 바나나축"
            );
            case NORMAL -> Arrays.asList(
                    "갈축",  "바나나축", "바닐라축",
                    "핑크축", "레몬축", "딸기축", "경해축",
                    "잉크축 V2", "모가축", "판다축",
                    "바다축", "라벤더축", "체리 스피드 실버",
                    "리니어 옵티컬"
            );
            case LOUD -> Arrays.asList(
                    "청축", "녹축","백축","clicky"
            );
            case SMOOTH -> Arrays.asList(
                    "적축", "흑축", "자석축", "광축",
                    "실버축", "스피드 적축", "잉크축",
                    "바다축", "바닐라축", "체리 리니어 옵티컬",
                    "라떼축", "모카축", "사파이어축","밀키축",
                    "무지개축", "크림축"
            );
        };

    }
    
    // Layout enum → description 검색 키워드 맵핑
    public static String mapLayoutToDescription(KeyboardOptions.Layout layout) {
        if (layout == null) return null;
        return switch (layout) {
            case ERGONOMIC -> "스텝스컬쳐2";
            case SIMPLE -> "로우프로파일(LP)";
        };
    }

    // 키압에서 g빼기
    public static Integer mapKeyPressureToSpecTable(KeyboardOptions.KeyPressure keyPressure) {
        if (keyPressure == null) return null;
        return switch (keyPressure) {
            case LIGHT -> 49;
            case NORMAL -> 50;
        };
    }
}

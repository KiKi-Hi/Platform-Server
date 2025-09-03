package site.kikihi.custom.global.response.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;

/// null 값은 직렬화에서 제외

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SliceResponse<T>(

        long totalCount,
        List<T> content,
        boolean hasNext,
        int page,
        int size
) {
    public static <T> SliceResponse<T> from(Slice<T> slice) {
        return SliceResponse.<T>builder()
                .content(slice.getContent())
                .hasNext(slice.hasNext())
                .page(slice.getNumber() + 1)
                .size(slice.getSize())
                .build();
    }

    public static <T> SliceResponse<T> from(Slice<T> slice, long totalCount) {
        return SliceResponse.<T>builder()
                .totalCount(totalCount)
                .content(slice.getContent())
                .hasNext(slice.hasNext())
                .page(slice.getNumber() + 1)
                .size(slice.getSize())
                .build();
    }

}

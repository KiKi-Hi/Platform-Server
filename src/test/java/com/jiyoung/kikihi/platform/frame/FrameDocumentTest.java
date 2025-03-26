package com.jiyoung.kikihi.platform.frame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jiyoung.kikihi.platform.adapter.out.mongo.product.FrameDocument;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import com.jiyoung.kikihi.platform.domain.product.frame.Layout;
import com.jiyoung.kikihi.platform.domain.product.frame.Material;
import com.jiyoung.kikihi.platform.domain.product.frame.MountType;
import org.junit.jupiter.api.Test;

public class FrameDocumentTest {

    @Test
    public void testToDomain() {
        // FrameDocument 객체를 만들어 테스트
        FrameDocument frameDocument = FrameDocument.builder()
                .layout(Layout.FULL) // 예시 값
                .materials(Material.ALUMINUM) // 예시 값
                .mountType(MountType.BOTTOM_MOUNT) // 예시 값
                .weight(1.2) // 예시 값
                .soundDampening("Silicon") // 예시 값
                .build();

        // toDomain() 메서드 호출
        Frame frame = frameDocument.toDomain();

        // 변환된 객체가 예상한 값과 동일한지 확인
        assertEquals(frame.getLayout(), Layout.FULL);
        assertEquals(frame.getMaterials(), Material.ALUMINUM);
        assertEquals(frame.getMountType(), MountType.BOTTOM_MOUNT);
        assertEquals(frame.getWeight(), 1.2);
        assertEquals(frame.getSoundDampening(), "Silicon");
    }
}

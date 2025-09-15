package site.kikihi.custom.platform.adapter.out.jpa.custom;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CustomKeyboardJpaRepository extends JpaRepository<CustomKeyboardJpaEntity, Long> {
    List<CustomKeyboardJpaEntity> findByIdIn(Set<Long> ids);

    Slice<CustomKeyboardJpaEntity> findKeyboardsByUserId(UUID userId);

    Optional<CustomKeyboardJpaEntity> findByUserId(UUID userId);

    /// 커스텀의 아이디 및 내부 상품 존재 여부 여러개 매핑
    boolean existsByIdAndFrameId(Long id, String frameId);

    boolean existsByIdAndSwitchId(Long id, String switchId);

    boolean existsByIdAndKeyCapId(Long id, String keyCapId);

    boolean existsByIdAndAccessoryId(Long id, String accessoryId);

    /// 해당 유저가 가지고 있는지 체크
    boolean existsByUserIdAndId(UUID userId, Long id);

    boolean existsByUserId(UUID userId);

}

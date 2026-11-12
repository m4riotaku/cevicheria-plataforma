package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.PreparationAreaKdsSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cevicheria.platform.model.id.PreparationAreaKdsSettingId;

public interface PreparationAreaKdsSettingRepository extends JpaRepository<PreparationAreaKdsSetting, PreparationAreaKdsSettingId> {
    
}

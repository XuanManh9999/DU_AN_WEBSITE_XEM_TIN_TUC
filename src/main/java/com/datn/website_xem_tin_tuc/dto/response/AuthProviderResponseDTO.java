package com.datn.website_xem_tin_tuc.dto.response;

import com.datn.website_xem_tin_tuc.enums.Active;
import lombok.Data;

@Data
public class AuthProviderResponseDTO {
   Long id;
   String providerName;
   Active active;
}

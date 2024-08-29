package com.moz.ates.traffic.admin.api;

import java.time.format.DateTimeParseException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.moz.ates.traffic.admin.api.dto.AuthRequest;
import com.moz.ates.traffic.admin.api.dto.AuthResponse;
import com.moz.ates.traffic.common.entity.api.ApiCommonResponse;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.support.exception.CommonException;

@RestController
@RequestMapping("/api/mozates")
public class ApiController {

	@Autowired
	ApiService apiService;
	
	final String authToken = "8c18c1d4-8a5c-45b4-9a50-81298f0cfb27";
	final String userName = "mozates";
	final String password = "ahwm54162dkxptm7162";

	/**
	 * @Method Name : login
	 * @Date : 2024. 6. 26.
	 * @Author : IK.MOON
	 * @Method Brief : token 발급
	 * @param map
	 * @return
	 */
	@PostMapping("/get-token")
	public ApiCommonResponse<AuthResponse> login(
			@RequestBody(required = false) Map<String, Object> paramMap) {

		ApiCommonResponse<AuthResponse> response = new ApiCommonResponse<AuthResponse>();
		AuthResponse data = new AuthResponse();
		AuthRequest request = new AuthRequest();

		if (paramMap == null) {
			response.setSuccess(false);
			// Request empty
			response.setMessage("Request vazia");
			return response;
		}

		request.setUsername(String.valueOf(paramMap.get("username")));
		request.setPassword(String.valueOf(paramMap.get("password")));

		// userName & password null check
		if (request.getUsername().equals("null")) {
			response.setSuccess(false);
			// username required
			response.setMessage("username obrigatório");
			return response;
		}
		if (request.getPassword().equals("null")) {
			response.setSuccess(false);
			// password required
			response.setMessage("password obrigatória");
			return response;
		}

		// userName & password matching check
		if (request.getUsername().equals(userName)) {
			if (request.getPassword().equals(password)) {
				data.setAccessToken(authToken);
				response.setSuccess(true);
				// Success to get token
				response.setMessage("Sucesso ao obter token");
				response.setData(data);

				return response;
			} else {
				response.setSuccess(false);
				// password mismatch
				response.setMessage("password não corresponde");
				return response;
			}
		} else {
			response.setSuccess(false);
			// username mismatch
			response.setMessage("username não corresponde");
			return response;
		}

	}

	/**
	 * @Method Name : statusUpdate
	 * @Date : 2024. 6. 26.
	 * @Author : IK.MOON
	 * @Method Brief :
	 * @param authorizationHeader
	 * @param paramMap
	 * @return
	 */
	@PostMapping("/update-payment-status")
	@ResponseBody
	public ApiCommonResponse<?> statusUpdate(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestBody(required = false) Map<String, Object> paramMap) {
		ApiCommonResponse<?> response = new ApiCommonResponse<Object>();

		// token 검증
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (!token.equals(authToken)) {
				response.setSuccess(false);
				// Invalid token
				response.setMessage("token inválido");
				return response;
			}
		} else {
			response.setSuccess(false);
			// token missing
			response.setMessage("Token ausente");
			return response;
		}
		
		// request 확인
		if (paramMap == null || paramMap.size() == 0) {
			response.setSuccess(false);
			// Request empty
			response.setMessage("Request vazia");
			return response;
		}
		
		// request key확인
		if (!paramMap.containsKey("nr_aviso") || !paramMap.containsKey("nome_do_pagador")
				|| !paramMap.containsKey("valor_do_pagamento")
				|| !paramMap.containsKey("data_de_pagamento")) {
      String missingFields = "";
      if (!paramMap.containsKey("nr_aviso")) {
          missingFields += "nr_aviso ";
      }
      if (!paramMap.containsKey("nome_do_pagador")) {
          missingFields += "nome_do_pagador ";
      }
      if (!paramMap.containsKey("valor_do_pagamento")) {
      	missingFields += "valor_do_pagamento ";
      }
      if (!paramMap.containsKey("data_de_pagamento")) {
      	missingFields += "data_de_pagamento ";
      }
			response.setSuccess(false);
			// Missing required fields:
			response.setMessage("Campos obrigatórios ausentes: " + missingFields.trim().replace(" ", ", "));
			return response;
		}
		
		// tfcEnfId null확인
		String tfcEnfId = (String) paramMap.get("nr_aviso");
		if (tfcEnfId == null) {
			response.setSuccess(false);
			// Invalid input: 'nr_aviso' must not be null
			response.setMessage("Entrada inválida: 'nr_aviso' não deve ser null");
			return response;
		}
		
		String datePattern = "yyyy-MM-dd HH:mm:ss";

		// request 값 검증
		MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
		try {
			finePymntInfo = apiService.checkRequestData(paramMap, datePattern);
		} catch (NumberFormatException e) {
			response.setSuccess(false);
			// Invalid input: The value for 'valor_do_pagamento' must be a float
			response.setMessage("Entrada inválida: O valor de 'valor_do_pagamento' deve ser um float");
			return response;
		} catch (DateTimeParseException e) {
			response.setSuccess(false);
			// Invalid input: The value for 'data_de_pagamento' must be a valid date in the format
			response.setMessage(String.format("Entrada inválida: O valor de 'data_de_pagamento' deve ser uma data válida no formato '%s'", datePattern));
			return response;
		} catch (CommonException e) {
			response.setSuccess(false);
			// Invalid input: 'nome_do_pagador' must not be null
			response.setMessage("Entrada inválida: 'nome_do_pagador' não deve ser null");
			return response;
		}

		// 결제정보 업데이트
		try {
			apiService.updateFinePymnt(finePymntInfo);
		} catch (CommonException e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		
		response.setSuccess(true);
		// update success
		response.setMessage(String.format("Atualização '%s' bem-sucedida", tfcEnfId));
		return response;

	}
}

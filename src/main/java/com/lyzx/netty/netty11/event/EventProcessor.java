package com.lyzx.netty.netty11.event;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyzx.netty.netty11.event.exceptions.MessageParseException;
import com.lyzx.netty.netty11.event.types.v1.*;
//import com.jufan.config.ServerConfig;
//import com.jufan.event.exceptions.MessageParseException;
//import com.jufan.event.types.v1.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author seven
 *
 */
public class EventProcessor {
//	private static Logger logger = LogManager.getLogger(EventProcessor.class);
//	private static Logger event_logger = LogManager.getLogger("com.jufan.event.EventProcessor.data");
//	private static Logger event_logger_test = LogManager.getLogger("com.jufan.event.EventProcessor.data.test");

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 *
	 * @param event
	 * @param clientIP
	 * @return
	 */
	public static String process(String event, String clientIP) {
		ProcessResult processResult = ProcessResult.OK;
		try{
			FrontMessage frontMessage = mapper.readValue(event, FrontMessage.class);

			addTime(frontMessage);
			addHost(frontMessage);
			addIp(frontMessage);
			addKey(frontMessage);
			addTopic(frontMessage);
			addLocationInfo(frontMessage, clientIP);
			addUserInfo(frontMessage);

			String target = mapper.writeValueAsString(frontMessage);
			if(frontMessage.getData().getEvent().getBiz_info().getBiz_id().endsWith("test")) {
				System.out.println("test:"+target);
			} else {
				System.out.println("formal:"+target);
			}
		} catch (JsonParseException e) {
			processResult = new ProcessResult("JsonParseException", e.getMessage());
		} catch (JsonMappingException e) {
			processResult = new ProcessResult("JsonMappingException", e.getMessage());
		} catch (IOException e) {
			processResult = new ProcessResult("IOException", e.getMessage());
		}

		String json = "";
		try{
			json = mapper.writeValueAsString(processResult);
		}catch(JsonProcessingException e) {
//			processResult = new ProcessResult("JsonParseException", processResult + " - " + e.getMessage());
//			logger.catching(e);
		}

		return json;
	}

	public static FrontMessage addTime(FrontMessage frontMessage) {
		frontMessage.setTime(System.currentTimeMillis());
		return frontMessage;
	}

	public static FrontMessage addHost(FrontMessage frontMessage) throws UnknownHostException {
		InetAddress ia = InetAddress.getLocalHost();
		String host = ia.getHostName();
		frontMessage.setHost(host);
		return frontMessage;
	}

	// TODO test ip
	public static FrontMessage addIp(FrontMessage frontMessage) {
		return frontMessage;
	}

	public static FrontMessage addKey(FrontMessage frontMessage) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		frontMessage.setKey("" + random.nextInt());
		return frontMessage;
	}

	public static FrontMessage addTopic(FrontMessage frontMessage) {
		BizInfo biz_info = frontMessage.getData().getEvent().getBiz_info();
		String platform = biz_info.getPlatform();
		String biz_id = biz_info.getBiz_id();
//		String topic = Constants.JUFAN + "." + platform + "." + biz_id;
		String topic = biz_id;
		frontMessage.setTopic(topic);
		return frontMessage;
	}

	// TODO IP -> CITY MAPPING
	public static FrontMessage addLocationInfo(FrontMessage frontMessage, String clientIP) {
		frontMessage.getData().getLocation().setIp(clientIP);
		return frontMessage;
	}

	public static FrontMessage addUserInfo(FrontMessage frontMessage) {
		User user = frontMessage.getData().getUser();
		String uid = frontMessage.getData().getUser().getUid();
		if (uid != null) {
			user.setId_type(UserIdTypes.UID);
			return frontMessage;
		}

		RunEnv runEnv = frontMessage.getData().getRun_env();
		DeviceInfo deviceInfo = runEnv.getDevice_info();
		String os = deviceInfo.getOs();
		String hid = null;
		if (OsTypes.ANDROID.equals(os)) {
			hid = deviceInfo.getMac();
		} else if (OsTypes.IOS.equals(os)) {
			hid = deviceInfo.getIdfv();
			if (hid == null) {
				hid = deviceInfo.getIdfa();
			}
		} else {
			throw new MessageParseException();
		}

		if (hid != null) {
			user.setId_type(UserIdTypes.HID);
			return frontMessage;
		}

		String tid = user.getT_uid();
		if(tid != null) {
			user.setId_type(UserIdTypes.TID);
			return frontMessage;
		}

		user.setId_type(UserIdTypes.UNKNOW);

		return frontMessage;
	}
}

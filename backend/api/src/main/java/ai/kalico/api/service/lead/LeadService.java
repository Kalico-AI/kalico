package ai.kalico.api.service.lead;

import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;

/**
 * @author Biz Melesse created on 2/5/23
 */
public interface LeadService {

  ChannelPageableResponse getChannelInfo(ChannelRequest channelRequest);

}

package annchain.genesis.sdk.core.protocol.core.methods.request;


import annchain.genesis.sdk.abi.EventEncoder;
import annchain.genesis.sdk.abi.TypeReference;
import annchain.genesis.sdk.abi.datatypes.Event;
import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.core.protocol.core.methods.CallBack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class EventCallBack implements CallBack {

    private Map<String,EventVo> events = new HashMap<>();

    public Map<String, EventVo> getEvents() {
        return events;
    }

    public void setEvents(EventVo eventVo) {
        this.events.put(eventVo.getTopic(),eventVo);
    }

    public class EventVo {
        private String eventName;
        private List<TypeReference<Type>> types;
        private String topic;

        public EventVo(String eventName, List<TypeReference<Type>> types) {
            this.eventName = eventName;
            this.types = types;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public List<TypeReference<Type>> getTypes() {
            return types;
        }

        public void setTypes(List<TypeReference<Type>> types) {
            this.types = types;
        }

        public String getTopic() {
            Event event = new Event(eventName, types, Arrays.asList());
            return EventEncoder.encode(event);
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
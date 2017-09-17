const Events = require('events');
const logger = require('log4js').getLogger("service EventsSet");

class EventsSet extends Events{
    constructor(){
        super();
    };
    add(...events){
        for(let event of events)
        {
            this.on(event,()=>{
                this.removeAllListeners(event);
                this.eventNames().length===0&&this.callback();
            })
        }
    }
    done(fun)
    {
        this.callback=fun;
    }
    callback()
    {
        logger.warn("no done register");
    }
}
module.exports = EventsSet;
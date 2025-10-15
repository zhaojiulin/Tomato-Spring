package com.banana.spring.handle;

import com.banana.spring.TomatoApplicationContext;

public interface ApplicationContextAware {
    void setApplicationContext(TomatoApplicationContext applicationContext);
}

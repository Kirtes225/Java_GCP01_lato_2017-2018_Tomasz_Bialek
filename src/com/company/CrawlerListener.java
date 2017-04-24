package com.company;

import com.company.events.CrawlerEvent;

public interface CrawlerListener {

    void actionPerformed(CrawlerEvent crawlerEvent);
}
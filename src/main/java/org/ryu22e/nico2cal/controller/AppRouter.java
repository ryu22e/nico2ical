package org.ryu22e.nico2cal.controller;

import org.slim3.controller.router.RouterImpl;

/**
 * URLルーティングを設定するクラス。
 * @author ryu22e
 *
 */
public class AppRouter extends RouterImpl {
    /**
     * コンストラクタ。
     */
    public AppRouter() {
        addRouting(
            "/ical/{startWeek}/{keyword}",
            "/Calendar?startWeek={startWeek}&keyword={keyword}");
        addRouting("/ical/{startWeek}", "/Calendar?startWeek={startWeek}");
    }
}

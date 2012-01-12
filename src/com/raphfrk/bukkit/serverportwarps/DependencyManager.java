/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.raphfrk.bukkit.serverportwarps;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.raphfrk.bukkit.eventlinkapi.EventLinkAPI;
import com.raphfrk.bukkit.serverportcoreapi.ServerPortCoreAPI;

public class DependencyManager {

	ServerPortWarps p;

	DependencyManager(ServerPortWarps p) {
		this.p = p;
	}

	boolean connectEventLink() {
		RegisteredServiceProvider<EventLinkAPI> eventLinkProvider = p.sm.getRegistration(EventLinkAPI.class);
		if(eventLinkProvider != null) {
			p.eventLink = eventLinkProvider.getProvider();
			if(p.eventLink != null) {
				p.log("Connected to Event Link API service");
				return true;

			}
		}
		p.log(p.pdfFile.getName() + " requires EventLink service provider");
		return false;
	}

	boolean connectServerPortCore() {

		RegisteredServiceProvider<ServerPortCoreAPI> serverPortCoreProvider = p.sm.getRegistration(ServerPortCoreAPI.class);
		if(serverPortCoreProvider != null) {
			p.serverPortCore = serverPortCoreProvider.getProvider();
			if(p.serverPortCore != null) {
				p.log("Connected to Server Port Core API service");
				return true;
			}
		} 
		p.log(p.pdfFile.getName() + " requires ServerPortCore service provider");
		return false;
	}

}

/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.charts.template

import com.dongxiguo.fastring.Fastring.Implicits._

import io.gatling.charts.report.Container.{ GROUP, REQUEST }

class MenuTemplate {
  def getOutput: Fastring = fast"""	
function getItemLink(item){
	return 'req_' + item.pathFormatted + '.html';
}

function setDetailsLinkUrl(){
    $$.each(stats.contents, function (name, data) {
        $$('#details_link').attr('href', getItemLink(data));
        return false;
    });
}

var MENU_ITEM_MAX_LENGTH = 50;

function menuItem(item, level, parent, group) {
    if (group)
        var style = 'group';
    else
        var style = '';

    if (item.name.length > MENU_ITEM_MAX_LENGTH) {
        var title = ' title="' + item.name + '"';
        var displayName = item.name.substr(0, MENU_ITEM_MAX_LENGTH) + '...';
    }
    else {
        var title = '';
        var displayName = item.name;
    }

    if (parent)
        var style = ' class="child-of-menu-' + parent + '"';
    else
        var style = '';

    if (group)
        var expandButton = '<span id="menu-' + item.pathFormatted + '" style="margin-left: ' + (level * 10) + 'px;" class="expand-button">&nbsp;</span>';
    else
        var expandButton = '<span id="menu-' + item.pathFormatted + '" style="margin-left: ' + (level * 10) + 'px;" class="expand-button hidden">&nbsp;</span>';

    return '<li' + style + '><div class="item">' + expandButton + '<a href="' + getItemLink(item) + '"' + title + '>' + displayName + '</a></div></li>';
}

function menuItemsForGroup(group, level, parent) {
    var items = '';

    if (level > 0)
        items += menuItem(group, level - 1, parent, true);

    $$.each(group.contents, function (contentName, content) {
        if (content.type == '$GROUP')
            items += menuItemsForGroup(content, level + 1, group.pathFormatted);
        else if (content.type == '$REQUEST')
            items += menuItem(content, level, group.pathFormatted);
    });

    return items;
}

function setDetailsMenu(){
    $$('.nav ul').append(menuItemsForGroup(stats, 0));

    $$('.nav').expandable();
}

function setGlobalMenu(){
    $$('.nav ul').append('<li><div class="item"><a href="#active_sessions">Active Sessions</a></div></li> \\
        <li><div class="item"><a href="#requests">Requests / sec</a></div></li> \\
        <li><div class="item"><a href="#responses">Responses / sec</a></div></li>');
}

function getLink(link){
    var a = link.split('/');
    return (a.length<=1)? link : a[a.length-1];
}
 
function setActiveMenu(){
    $$('.nav a').each(function(){
        if(!$$(this).hasClass('expand-button') && $$(this).attr('href') == getLink(window.location.pathname)){
            $$(this).parents('li').addClass('on');
            return false;
        }
    });
}
"""
}
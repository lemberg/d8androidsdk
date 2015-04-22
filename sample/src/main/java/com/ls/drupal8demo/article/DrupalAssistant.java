/*
 * The MIT License (MIT)
 *  Copyright (c) 2014 Lemberg Solutions Limited
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ls.drupal8demo.article;

import com.ls.drupal8demo.drupal.DrupalValueContainer;

import java.util.List;

/**
 * Created on 22.04.2015.
 */
public class DrupalAssistant {

    public static <T> T getValue(List<DrupalValueContainer<T>> list) {
        DrupalValueContainer<T> item = getItem(list);
        if (item != null) {
            return item.value;
        } else {
            return null;
        }
    }

    public static  <T> T getTargetId(List<DrupalValueContainer<T>> list) {
        DrupalValueContainer<T> item = getItem(list);
        if (item != null) {
            return item.target_id;
        } else {
            return null;
        }
    }

    public static  <T> DrupalValueContainer<T> getItem(List<DrupalValueContainer<T>> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}

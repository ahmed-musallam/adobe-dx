/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2020 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.dx.admin.config.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import com.day.cq.wcm.api.Page;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class },
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ColumnViewItem {

    private static final String ICON_FOLDER = "folder";

    private static final String ICON_CONFIG = "config";

    private static final Collection<String> IGNORED_NODES = Arrays.asList("jcr:content", "rep:policy", "workflow", "granite");

    private static final Collection<String> FOLDER_TYPES = Arrays.asList("nt:folder", "sling:Folder", "sling:OrderedFolder");

    @Self
    private Resource resource;

    @ValueMapValue(name = "jcr:primaryType")
    private String primaryType;

    @ValueMapValue(name = "jcr:title")
    private String title;

    private Page page;

    @PostConstruct
    private void init() {
        page = resource.adaptTo(Page.class);
    }

    public boolean getIsPage() {
        return page != null;
    }

    public String getLabel() {
        if (page != null && page.getTitle() != null) {
            return page.getTitle();
        }
        if (title != null) {
            return title;
        }
        return resource.getName();
    }

    public String getName() {
        return resource.getName();
    }

    public String getPath() {
        return resource.getPath();
    }

    public String getIconType() {
        if (primaryType != null && FOLDER_TYPES.contains(primaryType)) {
            return ICON_FOLDER;
        }
        return ICON_CONFIG;
    }

    public List<ColumnViewItem> getChildren() {
        List<ColumnViewItem> children = new ArrayList<>();
        Iterable<Resource> childrenIter = resource.getChildren();
        childrenIter.forEach(child -> {
            if (!IGNORED_NODES.contains(child.getName())) {
                ColumnViewItem item = child.adaptTo(ColumnViewItem.class);
                children.add(item);
            }
        });
        return children;
    }
}
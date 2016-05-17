/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package service;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;


@ManagedBean
public class ThemeSwitcherView {

    private List<PrimeTheme> themes;
    
    @ManagedProperty("#{themeService}")
    private ThemeService service;
    private String globalTheme = "afterdark";

    public String getGlobalTheme() {
        return globalTheme;
    }

    public void setGlobalTheme(String globalTheme) {
        this.globalTheme = globalTheme;
    }
    

    private PrimeTheme theme;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }
    
    public List<PrimeTheme> getThemes() {
        return themes;
    } 

    public void setService(ThemeService service) {
        this.service = service;
    }
    
    public void changeTheme() {
        String t = getTheme().getName();
        setGlobalTheme(t);
    }    

    /**
     * @return the theme
     */
    public PrimeTheme getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(PrimeTheme theme) {
        this.theme = theme;
    }
    
    
}

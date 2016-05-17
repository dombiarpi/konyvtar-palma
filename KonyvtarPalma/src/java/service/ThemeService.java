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
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import java.util.ArrayList;

@ManagedBean(name="themeService", eager = true)
@ApplicationScoped
public class ThemeService {
    
    private List<PrimeTheme> themes;
//    @Inject
//    private ThemeController themeController;
    
    @PostConstruct
    public void init() {
        
        themes = new ArrayList<PrimeTheme>();
        themes.add(new PrimeTheme(0, "Afterdark", "afterdark"));
        themes.add(new PrimeTheme(1, "Afternoon", "afternoon"));
        themes.add(new PrimeTheme(2, "Afterwork", "afterwork"));
        themes.add(new PrimeTheme(3, "Aristo", "aristo"));
        themes.add(new PrimeTheme(4, "Black-Tie", "black-tie"));
        themes.add(new PrimeTheme(5, "Blitzer", "blitzer"));
        themes.add(new PrimeTheme(6, "Bluesky", "bluesky"));
        themes.add(new PrimeTheme(7, "Bootstrap", "bootstrap"));
        themes.add(new PrimeTheme(8, "Casablanca", "casablanca"));
        themes.add(new PrimeTheme(9, "Cupertino", "cupertino"));
        themes.add(new PrimeTheme(10, "Cruze", "cruze"));
        themes.add(new PrimeTheme(11, "Dark-Hive", "dark-hive"));
        themes.add(new PrimeTheme(12, "Delta", "delta"));
        themes.add(new PrimeTheme(13, "Dot-Luv", "dot-luv"));
        themes.add(new PrimeTheme(14, "Eggplant", "eggplant"));
        themes.add(new PrimeTheme(15, "Excite-Bike", "excite-bike"));
        themes.add(new PrimeTheme(16, "Flick", "flick"));
        themes.add(new PrimeTheme(17, "Glass-X", "glass-x"));
        themes.add(new PrimeTheme(18, "Home", "home"));
        themes.add(new PrimeTheme(19, "Hot-Sneaks", "hot-sneaks"));
        themes.add(new PrimeTheme(20, "Humanity", "humanity"));
        themes.add(new PrimeTheme(21, "Le-Frog", "le-frog"));
        themes.add(new PrimeTheme(22, "Midnight", "midnight"));
        themes.add(new PrimeTheme(23, "Mint-Choc", "mint-choc"));
        themes.add(new PrimeTheme(24, "Overcast", "overcast"));
        themes.add(new PrimeTheme(25, "Pepper-Grinder", "pepper-grinder"));
        themes.add(new PrimeTheme(26, "Redmond", "redmond"));
        themes.add(new PrimeTheme(27, "Rocket", "rocket"));
        themes.add(new PrimeTheme(28, "Sam", "sam"));
        themes.add(new PrimeTheme(29, "Smoothness", "smoothness"));
        themes.add(new PrimeTheme(30, "South-Street", "south-street"));
        themes.add(new PrimeTheme(31, "Start", "start"));
        themes.add(new PrimeTheme(32, "Sunny", "sunny"));
        themes.add(new PrimeTheme(33, "Swanky-Purse", "swanky-purse"));
        themes.add(new PrimeTheme(34, "Trontastic", "trontastic"));
        themes.add(new PrimeTheme(35, "UI-Darkness", "ui-darkness"));
        themes.add(new PrimeTheme(36, "UI-Lightness", "ui-lightness"));
        themes.add(new PrimeTheme(37, "Vader", "vader"));
    }
    
    public List<PrimeTheme> getThemes() {
        return themes;
    } 
}

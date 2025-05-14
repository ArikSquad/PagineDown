/*
 * This file is part of PagineDown, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.paginedown;


import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class used to generate {@link net.kyori.adventure.text.Component} formatted chat menus of paginated list items.
 * <p>
 * A paginated list contains the following elements:
 * <ul>
 *     <li>A header, by default identifying the items listed on the current page</li>
 *     <li>The items, separated by new lines by default</li>
 *     <li>A footer, by default containing page navigation buttons and quick-jump page numbers</li>
 * </ul>
 * You can supply {@link ListOptions} to modify the format of each element, including shortcuts for modifying a theme color.
 * Once you have created a {@link PaginatedList} object using the static {@code get(items, options)},
 * you can easily generate a {@link net.kyori.adventure.text.Component} formatted chat menu for a given page using the {@code getPage()} methods.
 *
 * @author William278
 * @see ListOptions
 */
@SuppressWarnings("unused")
public class PaginatedList {

    /**
     * {@link ListOptions} to be used for generating the list
     */
    @NotNull
    private final ListOptions options;

    /**
     * A list of items to be paginated; can be MineDown formatted
     */
    @NotNull
    private final List<? extends Component> items;

    /**
     * Private constructor used by {@code #get(List, ListOptions)}
     *
     * @param items   a list of items to be paginated
     * @param options {@link ListOptions} to be used for generating list pages
     */
    private PaginatedList(@NotNull List<? extends Component> items, @NotNull ListOptions options) {
        this.items = items;
        this.options = options;
    }

    /**
     * Create a new {@link PaginatedList} from a {@link List} of items
     *
     * @param items The {@link List} of items to paginate
     * @return A new {@link PaginatedList}
     */
    @NotNull
    public static PaginatedList of(@NotNull List<? extends Component> items) {
        return new PaginatedList(items, new ListOptions.Builder().build());
    }

    /**
     * Create a new {@link PaginatedList} from a {@link List} of items
     *
     * @param items   The {@link List} of items to paginate
     * @param options The {@link ListOptions} to use for generating list pages
     * @return A new {@link PaginatedList}
     */
    @NotNull
    public static PaginatedList of(@NotNull List<? extends Component> items, @NotNull ListOptions options) {
        return new PaginatedList(items, options);
    }


    /**
     * Returns a {@link net.kyori.adventure.text.Component} formatted message to be sent to a player of the paginated list for the nearest specified page that exists
     * <p>List formats and options from the {@link ListOptions} are applied to generate list pages.
     * </p>{@code #getNearestValidPage()} will return the nearest valid page (i.e. values below 0 will be set to 0, values above the maximum page will be set to the maximum page)
     *
     * @param page The page number to get
     * @return A {@link net.kyori.adventure.text.Component} object, for formatting the list
     */
    @NotNull
    public Component getNearestValidPage(final int page) {
        return getPage(Math.max(1, Math.min(getTotalPages(), page)));
    }

    /**
     * Returns a {@link net.kyori.adventure.text.Component} formatted message to be sent to a player of the paginated list for the specified page
     * <p>List formats and options from the {@link ListOptions} are applied to generate the list.
     *
     * @param page The page number to get
     * @return A {@link net.kyori.adventure.text.Component} object, for formatting the list
     * @throws PaginationException If the page number is out of bounds
     */
    public Component getPage(final int page) throws PaginationException {
        return getRawPage(page);
    }

    /**
     * Generates a raw string of pre-{@link net.kyori.adventure.text.Component}-formatted text that when formatted will create the page menu.
     * <p>List formats and options from the {@link ListOptions} are applied to generate the list.
     *
     * @param page The page number to get
     * @throws PaginationException If the page number is out of bounds
     */
    @NotNull
    public Component getRawPage(final int page) throws PaginationException {
        if (page < 1) {
            throw new PaginationException("Page index must be >= 1");
        }
        if (page > getTotalPages()) {
            throw new PaginationException("Page index must be <= the total number of pages (" + getTotalPages() + ")");
        }

        List<Component> menuComponents = new ArrayList<>();

        if (!options.headerFormat.contains(Component.text(""))) {
            menuComponents.add(formatPageString(options.headerFormat, page));
            if (options.spaceAfterHeader) {
                menuComponents.add(Component.text(""));
            }
        }

        List<Component> itemComponents = getItemsForPage(page).stream().map(item -> formatPageString(item, page)).collect(Collectors.toList());

        Component itemsComponent = joinComponents(itemComponents, Component.text(options.itemSeparator));
        menuComponents.add(itemsComponent);

        if (!options.footerFormat.contains(Component.text(""))) {
            if (options.spaceBeforeFooter) {
                menuComponents.add(Component.text(""));
            }
            menuComponents.add(formatPageString(options.footerFormat, page));
        }

        return joinComponents(menuComponents, Component.newline());
    }

    /**
     * Returns the total number of pages
     *
     * @return The total number of pages
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / options.itemsPerPage);
    }

    /**
     * Returns the items to be displayed on the specified page
     *
     * @param page The page number to get
     * @return The sub-list of items to be shown on a given page
     */
    @NotNull
    private List<? extends Component> getItemsForPage(final int page) {
        return items.subList((page - 1) * options.itemsPerPage, Math.min(items.size(), page * options.itemsPerPage));
    }

    @NotNull
    private Component formatPageString(@NotNull Component component, int page) {
        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%color%").replacement(String.format("#%02x%02x%02x", options.themeColor.getRed(), options.themeColor.getGreen(), options.themeColor.getBlue()));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%first_item_on_page_index%").replacement(String.valueOf(((page - 1) * options.itemsPerPage) + 1));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%last_item_on_page_index%").replacement(String.valueOf(((page - 1) * options.itemsPerPage) + getItemsForPage(page).size()));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%total_items%").replacement(String.valueOf(items.size()));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%current_page%").replacement(String.valueOf(page));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%total_pages%").replacement(String.valueOf(getTotalPages()));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%next_page_index%").replacement(String.valueOf(page + 1));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%previous_page_index%").replacement(String.valueOf(page - 1));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%command%").replacement(options.command);
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%topic%").replacement((matchResult, builder) -> builder.append(formatPageString(options.topic, page)));
        });

        component = component.replaceText(configurer -> {
            configurer.matchLiteral("%previous_page_button%").replacement((matchResult, builder) -> {
                if (page > 1) {
                    builder.append(formatPageString(options.previousButtonFormat, page));
                }
                return builder;
            });
        });

        return component.replaceText(configurer -> {
            configurer.matchLiteral("%next_page_button%").replacement((matchResult, builder) -> {
                if (page < getTotalPages()) {
                    builder.append(formatPageString(options.nextButtonFormat, page));
                }
                return builder;
            });

            configurer.matchLiteral("%page_jumpers%").replacement((matchResult, builder) -> {
                if (getTotalPages() > 2) {
                    builder.append(formatPageString(options.pageJumpersFormat, page));
                }
                return builder;
            });

            configurer.matchLiteral("%page_jump_buttons%").replacement((matchResult, builder) -> builder.append(getPageJumperButtons(page)));
        });
    }

    /**
     * Formats a page jumper format with values
     *
     * @param page The page number of the jumper to format
     * @return The formatted page jumper
     */
    @NotNull
    private Component formatPageJumper(final int page) {
        return formatPageString(options.pageJumperPageFormat.replaceText(configurer -> {
            configurer.matchLiteral("%target_page_index%").replacement(Integer.toString(page));
        }), page);
    }

    @NotNull
    protected Component getPageJumperButtons(final int page) {
        Component result = Component.empty();
        Component groupSeparator = options.pageJumperGroupSeparator;
        Component pageSeparator = options.pageJumperPageSeparator;

        List<Component> pageGroups = new ArrayList<>();
        List<Component> pages = new ArrayList<>();
        int lastPage = 1;

        for (int i = 1; i <= getTotalPages(); i++) {
            if (i <= options.pageJumperStartButtons || i > getTotalPages() - options.pageJumperEndButtons || page == i) {
                if (i - lastPage > 1) {
                    // Add current page group and start a new one
                    if (!pages.isEmpty()) {
                        pageGroups.add(joinComponents(pages, pageSeparator));
                        pages = new ArrayList<>();
                    }
                }

                if (page == i) {
                    pages.add(formatPageString(options.pageJumperCurrentPageFormat, i));
                } else {
                    pages.add(formatPageJumper(i));
                }
                lastPage = i;
            }
        }

        if (!pages.isEmpty()) {
            pageGroups.add(joinComponents(pages, pageSeparator));
        }

        return joinComponents(pageGroups, groupSeparator);
    }

    @NotNull
    private Component joinComponents(List<Component> components, Component separator) {
        if (components.isEmpty()) {
            return Component.empty();
        }
        if (components.size() == 1) {
            return components.get(0);
        }

        Component result = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            result = result.append(separator).append(components.get(i));
        }
        return result;
    }

}

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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Options, including placeholder strings, used to generate a {@link PaginatedList} of items
 */
public class ListOptions {

    @NotNull
    protected Component headerFormat = MiniMessage.miniMessage().deserialize("[Viewing %topic%](%color%) [(%first_item_on_page_index%-%last_item_on_page_index% of](%color%) [%total_items%](%color% bold)[)](%color%)");
    @NotNull
    protected Component footerFormat = MiniMessage.miniMessage().deserialize("%previous_page_button%Page [%current_page%](%color%)/[%total_pages%](%color%)%next_page_button%   %page_jumpers%");
    @NotNull
    protected Component previousButtonFormat = MiniMessage.miniMessage().deserialize("[show_text=%color%View previous page \\(%previous_page_index%\\) run_command=/%command% %previous_page_index%) ");
    @NotNull
    protected Component nextButtonFormat = MiniMessage.miniMessage().deserialize(" [show_text=%color%View next page \\(%next_page_index%\\) run_command=/%command% %next_page_index%)");
    @NotNull
    protected Component pageJumpersFormat = MiniMessage.miniMessage().deserialize("(%page_jump_buttons%)");
    @NotNull
    protected Component pageJumperPageSeparator = MiniMessage.miniMessage().deserialize("|");
    @NotNull
    protected Component pageJumperGroupSeparator = MiniMessage.miniMessage().deserialize(")");
    @NotNull
    protected Component pageJumperCurrentPageFormat = MiniMessage.miniMessage().deserialize("[%current_page%](%color%)");
    @NotNull
    protected Component pageJumperPageFormat = MiniMessage.miniMessage().deserialize("[%target_page_index%](show_text=&7Jump to page %target_page_index% run_command=/%command% %target_page_index%)");
    @NotNull
    protected Component topic = Component.text("List");
    @NotNull
    protected String command = "example";
    @NotNull
    protected Color themeColor = new Color(0x00fb9a);
    protected boolean spaceAfterHeader = true;
    protected boolean spaceBeforeFooter = true;
    @NotNull
    protected String itemSeparator = "\n";
    protected int itemsPerPage = 10;

    protected int pageJumperStartButtons = 3;

    protected int pageJumperEndButtons = 3;

    private ListOptions() {
    }

    @SuppressWarnings("unused")
    public static class Builder {
        @NotNull
        private final ListOptions options = new ListOptions();

        @NotNull
        public Builder setHeaderFormat(@NotNull Component headerFormat) {
            options.headerFormat = headerFormat;
            return this;
        }

        @NotNull
        public Builder setFooterFormat(@NotNull Component footerFormat) {
            options.footerFormat = footerFormat;
            return this;
        }

        @NotNull
        public Builder setItemSeparator(@NotNull String itemSeparator) {
            options.itemSeparator = itemSeparator;
            return this;
        }

        @NotNull
        public Builder setThemeColor(@NotNull Color themeColor) {
            options.themeColor = themeColor;
            return this;
        }

        @NotNull
        public Builder setSpaceAfterHeader(final boolean spaceAfterHeader) {
            options.spaceAfterHeader = spaceAfterHeader;
            return this;
        }

        @NotNull
        public Builder setSpaceBeforeFooter(final boolean spaceBeforeFooter) {
            options.spaceBeforeFooter = spaceBeforeFooter;
            return this;
        }

        @NotNull
        public Builder setItemsPerPage(final int itemsPerPage) {
            options.itemsPerPage = itemsPerPage;
            return this;
        }

        @NotNull
        public Builder setTopic(@NotNull Component topic) {
            options.topic = topic;
            return this;
        }

        @NotNull
        public Builder setCommand(@NotNull String command) {
            options.command = command;
            return this;
        }

        @NotNull
        public Builder setPageJumpersFormat(@NotNull Component pageJumpersFormat) {
            options.pageJumpersFormat = pageJumpersFormat;
            return this;
        }

        @NotNull
        public Builder setPageJumperPageSeparator(@NotNull Component pageJumperPageSeparator) {
            options.pageJumperPageSeparator = pageJumperPageSeparator;
            return this;
        }

        @NotNull
        public Builder setPageJumperPageFormat(@NotNull Component pageJumperPageFormat) {
            options.pageJumperPageFormat = pageJumperPageFormat;
            return this;
        }

        @NotNull
        public Builder setPageJumperGroupSeparator(@NotNull Component pageJumperGroupSeparator) {
            options.pageJumperGroupSeparator = pageJumperGroupSeparator;
            return this;
        }

        @NotNull
        public Builder setPageJumperCurrentPageFormat(@NotNull Component pageJumperCurrentPageFormat) {
            options.pageJumperCurrentPageFormat = pageJumperCurrentPageFormat;
            return this;
        }

        @NotNull
        public Builder setPreviousButtonFormat(@NotNull Component previousButtonFormat) {
            options.previousButtonFormat = previousButtonFormat;
            return this;
        }

        @NotNull
        public Builder setNextButtonFormat(@NotNull Component nextButtonFormat) {
            options.nextButtonFormat = nextButtonFormat;
            return this;
        }

        @NotNull
        public Builder setPageJumperStartButtons(final int pageJumperStartButtons) {
            options.pageJumperStartButtons = pageJumperStartButtons;
            return this;
        }

        @NotNull
        public Builder setPageJumperEndButtons(final int pageJumperEndButtons) {
            options.pageJumperEndButtons = pageJumperEndButtons;
            return this;
        }

        @NotNull
        public ListOptions build() {
            return options;
        }
    }
}

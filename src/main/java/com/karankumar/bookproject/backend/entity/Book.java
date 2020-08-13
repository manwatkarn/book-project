/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true, exclude = "tags")
public class Book extends BaseEntity {
    @NotNull
    @NotEmpty
    private String title;
    private Integer numberOfPages;
    private Integer pagesRead;
    private Genre genre;
    private Integer seriesPosition;
    private String edition;
    private String bookRecommendedBy;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "author_id", referencedColumnName = "ID")
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "predefined_shelf_id", referencedColumnName = "ID")
    private PredefinedShelf predefinedShelf;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "custom_shelf_id", referencedColumnName = "ID")
    private CustomShelf customShelf;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "book_tag",
        joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags;

    // For books that have been read
    private RatingScale rating;
    private LocalDate dateStartedReading;
    private LocalDate dateFinishedReading;
    private String bookReview;

    public Book(String title, Author author, PredefinedShelf predefinedShelf) {
        this.title = title;
        this.author = author;
        this.predefinedShelf = predefinedShelf;
    }
    
    public void setEdition(Integer edition) {
        if (edition == null) {
            return;
        }
        String bookEdition = "";
        int lastDigit = edition % 10;
        switch (lastDigit) {
            case 1:
                bookEdition = getEdition() + "st edition";
                break;
            case 2:
                bookEdition = getEdition() + "nd edition";
                break;
            case 3:
                bookEdition = getEdition() + "rd edition";
                break;
            default:
                bookEdition = getEdition() + "th edition";
                break;
        }
        this.edition = bookEdition;
    }

    @Override
    public String toString() {
        return Book.class.getSimpleName() + "{"
            + "title='" + title + '\''
            + '}';
    }

    public boolean seriesPositionExists() {
        return seriesPosition != null && seriesPosition > 0;
    }
}

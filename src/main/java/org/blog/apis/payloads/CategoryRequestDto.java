package org.blog.apis.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 10,message = "title must be at least 10 character")
    private String categoryTitle;
    @NotBlank(message = " Category name is required")
    private String categoryName;
}

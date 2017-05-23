$(document).ready(function () {
    var max_fields = 10;
    var wrapper = $(".container1");
    var add_button = $(".add_author");
    var x = 1;
    $(add_button).click(function (e) {
        e.preventDefault();
        if (x < max_fields) {
            x++;
            $(wrapper).append('<div><a href="#" class="delete">-</a><input type="text" name="authorName[]" placeholder="Author"/></div>'.replace("[]", x+''));
        }
    });
    $(wrapper).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
        x--;
    })
});

$(document).ready(function () {
    var max_fields = 10;
    var wrapper = $(".container2");
    var add_button = $(".add_genre");
    var x = 2;
    $(add_button).click(function (e) {
        e.preventDefault();
        if (x < max_fields) {
            x++;
            $(wrapper).append('<div><a href="#" class="delete">-</a><input type="text" name="genreTitle[]" placeholder="Genre"/></div>'.replace("[]", x+''));
        }
    });
    $(wrapper).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
        x--;
    })
});


$(document).ready(function() {

  // Variables
  var $nav = $('.navbar'),
      $body = $('body'),
      $window = $(window),
      $popoverLink = $('[data-popover]'),
      navOffsetTop = $nav.offset().top,
      $document = $(document);

  function init() {
    $window.on('scroll', onScroll);
    $window.on('resize', resize);
    $popoverLink.on('click', openPopover);
    $document.on('click', closePopover);
    $('a[href*="#"]').on('click', smoothScroll);
    createBackToTopButton();
    reduceTableOfContents();
  }

  $(".hamburger").click(function() {
    if($('.navbar.responsive').length > 0) {
      $('.navbar').removeClass('responsive');
    } else {
      $('.navbar').addClass('responsive');
      scrollToNav();
    }
  });

  function scrollToNav() {
    $('body,html').animate({
      scrollTop: $('.navbar').position().top
    }, 800);
    return false;
  }

  function reduceTableOfContents() {
    var count = $("#markdown-toc li").length;
    if (count > 20) {
      $("#markdown-toc").addClass('reduce-toc');
      $("#markdown-toc").append('<li class="expand-link"><a href="#" class="expand-toc">Expand table of contents</a></li>')
      $(".expand-toc").click(function(e) {
        e.preventDefault();
        $("#markdown-toc").removeClass('reduce-toc');
      });
    }
  }

  function createBackToTopButton() {
  	var back_to_top_button = ['<a href="#top" title="Scroll to top" class="back-to-top">&and;</a>'].join("");
  	$("body").append(back_to_top_button);
  	$(".back-to-top").hide();

  	$(function () {
  		$(window).scroll(function () {
  			if ($(this).scrollTop() > 100) {
  				$('.back-to-top').fadeIn();
  			} else {
  				$('.back-to-top').fadeOut();
  			}
  		});

  		$('.back-to-top').click(function () {
  			$('body,html').animate({
  				scrollTop: 0
  			}, 800);
  			return false;
  		});
  	});
  }

  function smoothScroll(e) {
    e.preventDefault();
    $(document).off("scroll");
    var target = this.hash;
    var offset = $(window).width() > 750 ? -50 : 20;
    $('html, body').animate({
      scrollTop: $(target).offset().top + offset
    },'slow');
    window.location.hash = target;
    onScroll();
  }

  function openPopover(e) {
    e.preventDefault();
    closePopover();
    var popover = $($(this).data('popover'));
    popover.toggleClass('open');
    e.stopImmediatePropagation();
  }

  function closePopover(e) {
    if($('.popover.open').length > 0) {
      $('.popover').removeClass('open');
    }
  }

  $("#button").click(function() {
    $('html, body').animate({
        scrollTop: $("#elementtoScrollToID").offset().top
    }, 2000);
  });

  function resize() {
    $body.removeClass('has-docked-nav');
    navOffsetTop = $nav.offset().top;
    onScroll();
  }

  function onScroll() {
    if(navOffsetTop < $window.scrollTop() && !$body.hasClass('has-docked-nav')) {
      $body.addClass('has-docked-nav');
    }
    if(navOffsetTop > $window.scrollTop() && $body.hasClass('has-docked-nav')) {
      $body.removeClass('has-docked-nav');
    }
  }

  init();

});

{
    "use strict";

        var canvas = document.getElementById('myCanvas');
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.moveTo(20, 180); // Feder zum Startpunkt
        ctx.lineTo(40, 20); ctx.lineTo(140, 60); ctx.lineTo(180, 180);
        ctx.stroke()
}
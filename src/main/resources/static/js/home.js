
    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height");

    d3.json("../js/source.json", function (data) {
        var simulation = d3.forceSimulation()
            .force("link", d3.forceLink().id(function (d) {
                return d.id;
            }).distance(45))
            .force("charge", d3.forceManyBody().strength(-400))
            .force("center", d3.forceCenter(width / 2, height / 2));

        svg.append("svg:defs").append("svg:marker")
            .attr("id", "triangle")
            .attr("refX", 32)
            .attr("refY", 6)
            .attr("markerWidth", 30)
            .attr("markerHeight", 30)
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M 0 0 12 6 0 12 3 6")
            .style("fill", "#999");

        var link = svg.append("g")
            .attr("class", "links")
            .selectAll("line")
            .data(data.links)
            .enter().append("line")
            .attr("stroke-width", function (d) {
                return Math.sqrt(d.value);
            })
            .attr("marker-end", "url(#triangle)");


        var node = svg.selectAll(".node")
            .data(data.nodes)
            .enter().append("g")
            .attr("class", "node");


        node.append("circle")
            .attr("r", 20)
            .attr("fill", function (d) {
                if (d.state === "breakdown")
                    return "#C8553D"
                else if (d.state === "failure")
                    return "#F28F3B"
                return "#588B8B"
            });

        node.append("text")
            .attr("x", -21)
            .attr("y", 6)
            .attr("dx", 12)
            .text(function (d) {
                return d.name;
            });

        simulation
            .nodes(data.nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(data.links);

        function ticked() {
            link
                .attr("x1", function (d) {
                    return d.source.x;
                })
                .attr("y1", function (d) {
                    return d.source.y;
                })
                .attr("x2", function (d) {
                    return d.target.x;
                })
                .attr("y2", function (d) {
                    return d.target.y;
                });

            node.attr("transform", function (d) {
                return "translate(" + d.x + "," + d.y + ")";
            });
        }

        node = svg.selectAll(".node")
            .duration(750)
            .data(data.nodes)
    })



export default {
  data() {
    return {
      graph2d: null,
      graphs: {
        0: {
          items: [],
          groups: [
            {
              id: "0",
              content: "total",
              options: {
                excludeFromStacking: true,
              },
            },
          ],
          options: {
            style: "line",
            stack: false,
            legend: false,
            locale: "en",
            // zoomable: false,
            // barChart: { width: 50, align: 'center' },
            drawPoints: {
              onRender: function (item, group, grap2d) {
                return item.label != null;
              },
              style: "circle",
            },
            dataAxis: {
              icons: true,
              left: { range: { min: 0, max: 0 } },
            },
            height: 900,
            interpolation: false,
          },
        },
      },
      period: 0,
    };
  },
  components: {},
  props: [
    "showOption",
  ],
  watch: {

  },
  methods: {
    async search() {
      if (this.characterName == "") {
        return;
      }
      this.$emit("preSearch");
      setTimeout(() => this.search2(), 100);
    },
    async search2() {
      const resp = await fetch(
        `stats/records/${this.period}`
      );
      const body = await resp.json();
      const data = body;

      // this.graphs["0"].items = [];
      const items1 = this.graphs["0"].items;

      data.forEach((data) => {
        items1.push({
          x: data.timestamp,
          y: data.count,
        });
      });
      if (items1.length >= 0) {
        const container = document.getElementById("timeline");
        container.innerHTML = "";
        const g = this.graphs[0];
        const xRangeMin = Math.min.apply(null, items1.map(i => i.x));
        const xRangeMax = Math.max.apply(null, items1.map(i => i.x));
        const yRangeMax = Math.max.apply(null, items1.map(i => i.y)) * 1.1;

        this.graphs["0"].options.min = xRangeMin;
        this.graphs["0"].options.max = xRangeMax;
        this.graphs["0"].options.dataAxis.left.range.max = yRangeMax;

        this.graph2d = new vis.Graph2d(container, g.items, g.groups, g.options);
        this.graph2d.setWindow(xRangeMin, xRangeMax);
      }

      setTimeout(() => {
        this.period++;
        if (this.period <= 100) {
          this.search2();
        }
      }, 1000);
    },
    showOptionToggled() {
      this.$emit("showOptionToggled");
    },
  },
  template: `
    <v-container fluid>
    <v-row v-if="showOption" no-gutters>
    </v-row>
    <v-row no-gutters>
        <v-col>
            <v-btn class="ma-1" variant="outlined" @click="search">검색</v-btn>
            <v-btn class="ma-1" variant="outlined" @click="showOptionToggled">{{ showOption ? "검색 조건 닫기" : "검색 조건 열기" }}</v-btn>
        </v-col>
    </v-row>
    <v-row no-gutters>
        <v-col>
            <div id="timeline"></div>
        </v-col>
    </v-row>
    </v-container>
    `,
};

export default {
  data() {
    return {
      minimumRun: 1,
      players: new vis.DataSet([]),
      relations: new vis.DataSet([]),
      nextId: 1,
    };
  },
  components: {},
  props: ["serverNames", "characters", "characterName", "server", "showOption"],
  methods: {
    search() {
      if (this.characterName == "") {
        return;
      }

      this.$emit("preSearch");
      if (window.localStorage) {
        window.localStorage.setItem("relation_run", this.minimumRun);
      }
      setTimeout(() => this.search2(), 100);
    },
    search2() {
      fetch(
        "char/relation/" +
          encodeURI(this.server) +
          "/" +
          encodeURI(this.characterName) +
          "/" +
          this.minimumRun
      ).then(async (resp) => {
        const data = await resp.json();
        let i1 = this.players
          .get()
          .find((p) => p.label == this.characterName + "-" + this.server);
        if (i1 === undefined) {
          i1 = {
            id: this.nextId++,
            label: this.characterName + "-" + this.server,
          };
          this.players.add(i1);
        }

        data.forEach((dd) => {
          if (dd.value >= this.minimumRun) {
            let i2 = this.players
              .get()
              .find((p) => p.label == dd.name + "-" + dd.realm);
            if (i2 === undefined) {
              i2 = {
                id: this.nextId++,
                label: dd.name + "-" + dd.realm,
              };
              this.players.add(i2);
            }

            var exists = this.relations
              .get()
              .some(
                (rel) =>
                  (rel.from == i1.id && rel.to == i2.id) ||
                  (rel.from == i2.id && rel.to == i1.id)
              );
            // this.relations.forEach(rel => {
            //     if (rel.from == i1.id && rel.to == i2.id) {
            //         exists = true
            //     } else if (rel.from == i2.id && rel.to == i1.id) {
            //         exists = true
            //     }
            // })
            if (!exists) {
              this.relations.add({
                from: i1.id,
                to: i2.id,
                value: dd.value,
                title: dd.value,
              });
            }
          }
        });
      });
    },
    quickSearch(index) {
      this.$emit("characterSelected", index);
      this.search();
    },
    nameUpdated(event) {
      this.$emit("nameUpdated", event);
    },
    serverUpdated(event) {
      this.$emit("serverUpdated", event);
    },
    showOptionToggled() {
      this.$emit("showOptionToggled");
    },
  },
  beforeMount() {
    if (window.localStorage) {
      const run = parseInt(window.localStorage.getItem("relation_run"));
      if (run > 0) {
        this.minimumRun = run;
      }
    }
  },
  mounted() {
    var container = document.getElementById("relations");
    var data = {
      nodes: this.players,
      edges: this.relations,
    };
    var options = {};
    var network = new vis.Network(container, data, options);
    network.on("doubleClick", (e) => {
      if (e.nodes.length > 0) {
        var id = e.nodes[0];
        var pl = this.players.get().find((p) => p.id == id);
        var name = pl.label;
        this.nameUpdated(name.substring(0, name.indexOf("-")));
        this.serverUpdated(name.substring(name.indexOf("-") + 1));
        this.search();
      }
    });
  },
  template: `
    <v-container fluid>
        <v-row v-if="showOption" no-gutters>
            <v-col cols="12" sm="5">
                <v-text-field type="text" label="캐릭터명" :modelValue="characterName" @update:modelValue="nameUpdated" />
            </v-col>
            <v-col cols="12" sm="4">
                <v-combobox id="server" label="서버" :items="serverNames" :modelValue="server" @update:modelValue="serverUpdated" />
            </v-col>
            <v-col cols="12" sm="3">
                <v-text-field type="number" label="최소 파티횟수" v-model="minimumRun" min="1" max="10000" ></v-text-field>
            </v-col>
        </v-row>
        <v-row no-gutters>
            <v-col>
            <v-menu v-if="characters.length > 0">
            <template v-slot:activator="{ props }">
              <v-btn variant="outlined" v-bind="props">선택검색</v-btn>
            </template>
            <v-list>
              <v-list-item v-for="(item, index) in characters" :key="index" :value="index" @click="quickSearch(index)">
                <v-list-item-title>{{ item.name }} - {{ item.server }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
                <v-btn class="ma-1" variant="outlined" @click="search">검색</v-btn>
                <v-btn class="ma-1" variant="outlined" @click="showOptionToggled">{{ showOption ? "검색 조건 닫기" : "검색 조건 열기" }}</v-btn>
            </v-col>
        </v-row>
        <v-row no-gutters style="background-color: #f0f0f0">
            <v-col>
                <div id="relations" style="height: calc(100vh - 100px)"></div>
            </v-col>
        </v-row>
    </v-container>
    `,
};

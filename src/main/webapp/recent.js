import Utils from "./utils.js";

export default {
  data() {
    return {
      player: null,
      records: [],
    };
  },
  components: {},
  props: [
    "serverNames",
    "dungeons",
    "characters",
    "characterName",
    "server",
    "showOption",
  ],
  methods: {
    search() {
      if (this.characterName == "") {
        return;
      }
      this.$emit("preSearch");

      setTimeout(() => {
        fetch(
          "char/recent/" +
            encodeURI(this.server) +
            "/" +
            encodeURI(this.characterName)
        ).then(async (resp) => {
          const data = await resp.json();
          this.player = data.player;
          this.records = data.data;
        });
      }, 100);
    },
    scan() {
      if (this.characterName == "") {
        return;
      }
      this.$emit("preSearch");

      setTimeout(() => {
        fetch(
          "char/scan/" +
            encodeURI(this.server) +
            "/" +
            encodeURI(this.characterName)
        );
      }, 100);
    },
    quickSearch(index) {
      this.$emit("characterSelected", index);
      this.search();
    },
    formatDate(d) {
      return Utils.formatDate(d);
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
  mounted() {},
  template: `
    <v-container fluid>
        <v-row v-if="showOption" no-gutters>
            <v-col cols="12" sm="6">
                <v-text-field type="text" label="캐릭터명" :modelValue="characterName" @update:modelValue="nameUpdated" />
            </v-col>
            <v-col cols="12" sm="6">
                <v-combobox id="server" label="서버" :items="serverNames" :modelValue="server" @update:modelValue="serverUpdated" />
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
                <v-btn class="ma-1" variant="outlined" @click="scan">재검사</v-btn>
            </v-col>
        </v-row>
        <v-row no-gutters v-if="player != null">
            <v-col>
                <div>마지막 갱신 일자: {{ formatDate(player?.lastUpdateTs ?? 0) }}</div>
            </v-col>
        </v-row>
        <v-row v-for="record in records" no-gutters style="background-color: #f0f0f0">
            <v-col>
                {{ formatDate(record.completedTimestamp) }}
            </v-col>
            <v-col>
                {{ dungeons[record.dungeonId] }}
            </v-col>
            <v-col>
                {{ record.keystoneLevel }}
            </v-col>
            <v-col>
                {{ record.keystoneUpgrade }}
            </v-col>
        </v-row>
    </v-container>
    `,
};

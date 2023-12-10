export default {
  data() {
    return {
      characterName: "",
      server: "",
    };
  },
  components: {},
  props: ["serverNames", "characters"],
  methods: {
    insert() {
      if (this.characterName == "") {
        return;
      }
      this.characterName =
        this.characterName.substring(0, 1).toUpperCase() +
        this.characterName.substring(1).toLowerCase();

      this.$emit("added", {
        name: this.characterName,
        server: this.server,
      });
    },
    remove(index) {
      this.$emit("removed", index);
    },
  },
  beforeMount() {
    if (window.localStorage) {
      const server = window.localStorage.getItem("relation_server");
      const run = parseInt(window.localStorage.getItem("relation_run"));
      this.server = server;
      this.characterName = window.localStorage.getItem("relation_character");
    }
    if (this.server == "" || this.server == null) {
      this.server = "아즈샤라";
    }
  },
  mounted() {},
  template: `
    <v-container fluid>
        <v-row>
            <v-col cols="12" sm="5">
                <v-text-field type="text" label="캐릭터명" v-model="characterName" />
            </v-col>
            <v-col cols="8" sm="4">
                <v-combobox id="server" label="서버" :items="serverNames" v-model="server" />
            </v-col>
            <v-col cols="4" sm="3">
                <v-btn class="ma-1" variant="outlined" @click="insert">추가</v-btn>
            </v-col>
        </v-row>
        <v-divider></v-divider>
        <v-row v-for="(item, index) in characters">
            <v-col cols="5">
                {{ item.name }}
            </v-col>
            <v-col cols="4">
                {{ item.server }}
            </v-col>
            <v-col cols="3">
                <v-btn class="ma-1" variant="outlined" @click="remove(index)">삭제</v-btn>
            </v-col>
        </v-row>
    </v-container>
    `,
};

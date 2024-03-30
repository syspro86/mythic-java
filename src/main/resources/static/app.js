import Login from "./login.js";
import CharacterList from "./character-list.js";
import Recent from "./recent.js";
import Relation from "./relation.js";
import ScoreTimeline from "./score-timeline.js";
import RecordCountChart from "./record-count-chart.js";

export default {
  data() {
    return {
      menuIndex: 1,
      drawerVisible: false,
      showOption: true,
      characterName: "",
      server: "",
      servers: {},
      serverNames: [],
      dungeons: [],
      characters: [],
    };
  },
  components: {
    Login,
    CharacterList,
    Recent,
    Relation,
    ScoreTimeline,
    RecordCountChart,
  },
  methods: {
    menuSelected(index) {
      this.menuIndex = index;
      this.drawerVisible = !this.$vuetify.display.mobile;
    },
  },
  beforeMount() {
    this.drawerVisible = !this.$vuetify.display.mobile;

    if (window.localStorage) {
      this.server = window.localStorage.getItem("relation_server");
      this.characterName = window.localStorage.getItem("relation_character");
    }
    if (this.server == "" || this.server == null) {
      this.server = "아즈샤라";
    }

    const characters = window.localStorage.getItem("characters");
    try {
      this.characters = JSON.parse(characters);
      if (this.characters.length > 0) {
        this.server = this.characters[0].server;
      }
    } catch (e) {
      this.characters = [];
    }

    const urlParams = new URLSearchParams(window.location.search);
    const menu = urlParams.get("menu");
    switch (menu) {
      case "recent":
        this.menuIndex = 1;
        break;
      case "relation":
        this.menuIndex = 2;
        break;
      case "timeline":
        this.menuIndex = 3;
        break;
      case "record-timeline":
        this.menuIndex = 4;
        break;
    }
  },
  mounted() {
    fetch("form/realms").then(async (resp) => {
      this.servers = await resp.json();
      for (var key in this.servers) {
        this.serverNames.push(this.servers[key]);
      }
      if (this.server == "") {
        this.server = this.serverNames[0]["value"];
      }
    });
    fetch("form/dungeons").then(async (resp) => {
      this.dungeons = await resp.json();
    });
  },
  computed: {
    menus() {
      return [
        {
          title: "캐릭터 목록",
          component: CharacterList,
          props: {
            serverNames: this.serverNames,
            characters: this.characters,
            characterName: this.characterName,
            server: this.server,
          },
          event: {
            added: (item) => {
              if (
                this.characters.some(
                  (c) => c.name == item.name && c.server == item.server
                )
              ) {
                return;
              }
              this.characters.push(item);

              if (window.localStorage) {
                window.localStorage.setItem(
                  "characters",
                  JSON.stringify(this.characters)
                );
              }
            },
            removed: (index) => {
              this.characters.splice(index, 1);

              if (window.localStorage) {
                window.localStorage.setItem(
                  "characters",
                  JSON.stringify(this.characters)
                );
              }
            },
          },
        },
        {
          title: "쐐기 기록",
          url: "recent.html",
          component: Recent,
          props: {
            serverNames: this.serverNames,
            dungeons: this.dungeons,
            characters: this.characters,
            characterName: this.characterName,
            server: this.server,
            showOption: this.showOption,
          },
          event: {
            nameUpdated: (characterName) => {
              this.characterName = characterName;
            },
            serverUpdated: (server) => {
              this.server = server;
            },
            characterSelected: (index) => {
              this.characterName = this.characters[index].name;
              this.server = this.characters[index].server;
            },
            showOptionToggled: () => {
              this.showOption = !this.showOption;
            },
            preSearch: () => {
              this.characterName =
                this.characterName.substring(0, 1).toUpperCase() +
                this.characterName.substring(1).toLowerCase();

              if (window.localStorage) {
                window.localStorage.setItem("relation_server", this.server);
                window.localStorage.setItem(
                  "relation_character",
                  this.characterName
                );
              }
            },
          },
        },
        {
          title: "친구 그래프",
          url: "relation.html",
          component: Relation,
          props: {
            serverNames: this.serverNames,
            characters: this.characters,
            characterName: this.characterName,
            server: this.server,
            showOption: this.showOption,
          },
          event: {
            nameUpdated: (characterName) => {
              this.characterName = characterName;
            },
            serverUpdated: (server) => {
              this.server = server;
            },
            characterSelected: (index) => {
              this.characterName = this.characters[index].name;
              this.server = this.characters[index].server;
            },
            showOptionToggled: () => {
              this.showOption = !this.showOption;
            },
            preSearch: () => {
              this.characterName =
                this.characterName.substring(0, 1).toUpperCase() +
                this.characterName.substring(1).toLowerCase();

              if (window.localStorage) {
                window.localStorage.setItem("relation_server", this.server);
                window.localStorage.setItem(
                  "relation_character",
                  this.characterName
                );
              }
            },
          },
        },
        {
          title: "쐐기평점 타임라인",
          url: "score-timeline.html",
          component: ScoreTimeline,
          props: {
            serverNames: this.serverNames,
            dungeons: this.dungeons,
            characters: this.characters,
            characterName: this.characterName,
            server: this.server,
            showOption: this.showOption,
          },
          event: {
            nameUpdated: (characterName) => {
              this.characterName = characterName;
            },
            serverUpdated: (server) => {
              this.server = server;
            },
            characterSelected: (index) => {
              this.characterName = this.characters[index].name;
              this.server = this.characters[index].server;
            },
            showOptionToggled: () => {
              this.showOption = !this.showOption;
            },
            preSearch: () => {
              this.characterName =
                this.characterName.substring(0, 1).toUpperCase() +
                this.characterName.substring(1).toLowerCase();

              if (window.localStorage) {
                window.localStorage.setItem("relation_server", this.server);
                window.localStorage.setItem(
                  "relation_character",
                  this.characterName
                );
              }
            },
          },
        },
        {
          title: "쐐기수집건수 차트",
          component: RecordCountChart,
          props: {
            showOption: this.showOption,
          },
          event: {
            preSearch: () => {
            },
          },
        },
      ];
    },
  },
  template: `
    <v-app>
      <v-container fluid>
        <v-app-bar :elevation="2" color="primary">
          <template v-slot:prepend>
            <v-app-bar-nav-icon @click="drawerVisible = !$vuetify.display.mobile || !drawerVisible"></v-app-bar-nav-icon>
          </template>
          <v-app-bar-title>{{ menus[menuIndex].title }}</v-app-bar-title>
        </v-app-bar>
        <v-navigation-drawer v-model="drawerVisible">
          <v-list-item title="User" nav>
            <Login/>
            <template v-slot:append v-if="$vuetify.display.mobile">
              <v-btn variant="text" icon="mdi-chevron-left" @click="drawerVisible = false"></v-btn>
            </template>
          </v-list-item>
          <v-divider></v-divider>
          <v-list density="compact" nav>
            <v-list-item v-for="(item, index) in menus" :key="index" :value="index" @click="menuSelected(index)" :title="item.title">
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
        </v-navigation-drawer>
        <v-main>
          <component :is="menus[menuIndex].component" v-bind="menus[menuIndex].props" v-on="menus[menuIndex].event" />
        </v-main>
      </v-container>
    </v-app>
    `,
};
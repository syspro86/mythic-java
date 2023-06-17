import Login from './login.js'
import Menu from './menu.js'
import Utils from './utils.js'

export default {
    data() {
        return {
            'characterName': '',
            'server': '',
            'servers': {},
            'serverNames': [],
            'dungeons': {},
            'player': null,
            'records': [],
            'showOption': true,
        }
    },
    components: {
        Login, Menu
    },
    methods: {
        search() {
            if (this.characterName == '') {
                return;
            }
            this.characterName = this.characterName.substring(0, 1).toUpperCase() +
                this.characterName.substring(1).toLowerCase();

            if (window.localStorage) {
                window.localStorage.setItem('relation_server', this.server);
                window.localStorage.setItem('relation_character', this.characterName);
            }

            fetch('char/recent/' + encodeURI(this.server) + '/' + encodeURI(this.characterName)).then(async resp => {
                const data = await resp.json();
                this.player = data.player;
                this.records = data.data;
            });
        },
        scan() {
            if (this.characterName == '') {
                return;
            }
            this.characterName = this.characterName.substring(0, 1).toUpperCase() +
                this.characterName.substring(1).toLowerCase();

            fetch('char/scan/' + encodeURI(this.server) + '/' + encodeURI(this.characterName));
        },
        formatDate(d) {
            return Utils.formatDate(d);
        },
    },
    beforeMount() {
        if (window.localStorage) {
            this.server = window.localStorage.getItem('relation_server')
            this.characterName = window.localStorage.getItem('relation_character')
        }
        if (this.server == '' || this.server == null) {
            this.server = '아즈샤라';
        }
    },
    mounted() {
        fetch('form/realms').then(async resp => {
            this.servers = await resp.json()
            for (var key in this.servers) {
                this.serverNames.push(this.servers[key]);
            }
            if (this.server == '') {
                this.server = this.serverNames[0]['value']
            }
        });
        fetch('form/dungeons').then(async resp => {
            this.dungeons = await resp.json()
        })
    },
    template: `
    <v-container fluid>
        <v-row v-if="showOption" no-gutters>
            <v-col cols="12" sm="6">
                <v-text-field type="text" label="캐릭터명" v-model="characterName" />
            </v-col>
            <v-col cols="12" sm="6">
                <v-combobox id="server" label="서버" :items="serverNames" v-model="server" />
            </v-col>
        </v-row>
        <v-row no-gutters>
            <v-col>
                <v-btn class="ma-1" variant="outlined" @click="search">검색</v-btn>
                <v-btn class="ma-1" variant="outlined" @click="showOption=!showOption">{{ showOption ? "검색 조건 닫기" : "검색 조건 열기" }}</v-btn>
                <Menu/>
                <Login/>
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
}

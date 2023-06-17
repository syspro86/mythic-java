export default {
    formatDate(d) {
        if (typeof (d) == 'number') {
            d = new Date(d);
        }
        const year = d.getYear() + 1900;
        const month = d.getMonth() + 1;
        const day = d.getDay();
        const hour = d.getHours();
        const minute = d.getMinutes();
        const second = d.getSeconds();
        return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    }
}

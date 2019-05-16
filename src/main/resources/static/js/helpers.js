function deepClone(obj) {
    return JSON.parse(JSON.stringify(obj));
}

function objDiff(from, to) {
    const diff = {};

    Object.entries(to).map(function ([key, value]) {
        if (from.hasOwnProperty(key) && from[key] != value) {
            diff[key] = value;
        }
    });

    return diff;
}

function appendZeroes(n) {
    if (n <= 9) {
        return "0" + n;
    }
    return n;
}

function formatDate(string) {
    let date = new Date(string);

    let year = date.getFullYear();
    let month = this.appendZeroes(date.getMonth() + 1);
    let day = this.appendZeroes(date.getDate());

    return year + "-" + month + "-" + day;
}

function unformatDate(date) {
    const parts = date.split("-");

    return new Date(parts[0], parts[1] - 1, parts[2]);
}

export const formatDate = (dateString) => {
    const options = { year: '2-digit', month: '2-digit', day: '2-digit' };
    const formattedDate = new Intl.DateTimeFormat('en-GB', options).format(new Date(dateString));
    return formattedDate.replace(/\//g, '.');
};


export const timeSince = (date) => {
    const now = new Date();
    const secondsPast = (now.getTime() - date.getTime()) / 1000;

    if (secondsPast < 60) {
        return `${parseInt(secondsPast)}s ago`;
    }
    if (secondsPast < 3600) {
        return `${parseInt(secondsPast / 60)}m ago`;
    }
    if (secondsPast <= 86400) {
        return `${parseInt(secondsPast / 3600)}h ago`;
    }
    if (secondsPast > 86400) {
        const day = date.getDate();
        const month = date.toDateString().match(/ [a-zA-Z]*/)[0].replace(" ", "");
        const year = date.getFullYear() == now.getFullYear() ? "" : ` ${date.getFullYear()}`;
        return `${day} ${month}${year}`;
    }
};



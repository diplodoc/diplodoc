function buildURI() {
    var token = null;

    if (hello('google').getAuthResponse()) {
        token = hello('google').getAuthResponse().access_token;
    }

    console.log(token);
    if (token) {
        return "http://localhost:8080/modules-java/client/feeder/feed?size=16&auth_provider=google&auth_type=access_token&auth_token=" + token + "&page="
    }
    return "http://localhost:8080/modules-java/client/feeder/feed?size=16&page="
}

url = buildURI()

var DiploCard = React.createClass({
    render: function () {
        console.log("ID" + this.props.id);
        return (
            <li>
            <div className="card blue-grey darken-1">
                <div className="card-content white-text">
                    <span className="card-title">{this.props.title}</span>
                    <p>{this.props.description}</p>
                </div>
                <div className="card-action">
                    <a href={this.props.url}>Read more...</a>
                </div>
            </div>
            </li>
        );
    }
});

var DiploCardList = React.createClass({
    loadMoreCommentsFromServer: function () {
        if (this.state.page < 10) {
            $.ajax({
                url: url + this.state.page.toString(),
                dataType: 'json',
                success: function (data) {
                    this.setState({data: this.state.data.concat(data), page: this.state.page + 1});
                }.bind(this),
                error: function (xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
        }
        // this.setState({data: [{title: 'f', description: 'd', url: 'f'}, {title: 'f', description: 'd', url: 'f'}, {title: 'f', description: 'd', url: 'f'}]});
    },

    getInitialState: function () {
        return {data: [], page: 1};
    },

    componentDidMount: function () {
        //this.loadMoreCommentsFromServer();
        setInterval(this.loadMoreCommentsFromServer, 5000);
    },

    render: function () {
        var card_list = [];
        var i = 0;
        while (i < this.state.data.length) {
            var data_elem = this.state.data[i];
            console.log(data_elem);
            card_list.push(<DiploCard title={data_elem.title} description="" url={data_elem.url} id={data_elem.id}/>);
            i++;
        }
        console.log("HERE");
        return (
            <ul className="grid effect-1" id="grid">
                {card_list}
            </ul>
        );
    }
});

var token = null;

if (hello('google').getAuthResponse()) {
    token = hello('google').getAuthResponse().access_token;
}
if (token) {
    document.onload = React.render(
        <DiploCardList />,
        document.getElementById('root')
    );
}

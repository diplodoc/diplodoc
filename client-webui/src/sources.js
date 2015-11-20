function topPosition(domElt) {
    if (!domElt) {
        return 0;
    }
    return domElt.offsetTop + topPosition(domElt.offsetParent);
}

var InfiniteScroll = React.createClass({
    getDefaultProps: function () {
        return {
            pageStart: 0,
            hasMore: false,
            loadMore: function () {
            },
            threshold: 250
        };
    },
    componentDidMount: function () {
        this.pageLoaded = this.props.pageStart;
        this.attachScrollListener();
    },
    componentDidUpdate: function () {
        this.attachScrollListener();
    },
    render: function () {
        var props = this.props;
        return React.DOM.div(null, props.children, props.hasMore && (props.loader || InfiniteScroll._defaultLoader));
    },
    scrollListener: function () {
        var el = this.getDOMNode();
        var scrollTop = (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop;
        if (topPosition(el) + el.offsetHeight - scrollTop - window.innerHeight < Number(this.props.threshold)) {
            this.detachScrollListener();
            // call loadMore after detachScrollListener to allow
            // for non-async loadMore functions
            this.props.loadMore(this.pageLoaded += 1);
        }
    },
    attachScrollListener: function () {
        if (!this.props.hasMore) {
            return;
        }
        window.addEventListener('scroll', this.scrollListener);
        window.addEventListener('resize', this.scrollListener);
        this.scrollListener();
    },
    detachScrollListener: function () {
        window.removeEventListener('scroll', this.scrollListener);
        window.removeEventListener('resize', this.scrollListener);
    },
    componentWillUnmount: function () {
        this.detachScrollListener();
    }
});
InfiniteScroll.setDefaultLoader = function (loader) {
    InfiniteScroll._defaultLoader = loader;
};

function buildURI() {
    var token = null;

    if (hello('google').getAuthResponse()) {
        token = hello('google').getAuthResponse().access_token;
    }

    console.log(token);
    if (token) {
        return "${modules_host}/client-api/sources?size=16&auth_provider=google&auth_type=access_token&auth_token=" + token + "&page="
    }
    return "${modules_host}/client-api/sources?size=16&page="
}

url = buildURI()

var DiploPanel = React.createClass({
    render: function () {
        var divStyle = {
            height: '100px',
            overflow: 'hidden'
        };

        return (
            <div className="card blue-grey darken-1">
                <div style={divStyle} className="card-content white-text">
                    <span className="card-title">{this.props.title}</span>
                </div>
                <div className="card-action">
                    <a href={this.props.rssUrl}>{this.props.rssUrl}</a>
                </div>
            </div>
        );
    }
});

var DiploPanelBlock = React.createClass({

    styleConfig: function (style) {
        switch (style) {
            case '6-3-3':
                return ["col s6", "col s3", "col s3"];
            case '3-6-3':
                return ["col s3", "col s6", "col s3"];
            case '3-3-6':
                return ["col s3", "col s3", "col s6"];
            case '4-8':
                return ["col s4", "col s8"];
            case '8-4':
                return ["col s8", "col s4"];
            default:
                // 4-4-4
                return ["col s4", "col s4", "col s4"];
        }
    },

    render: function () {
        var config = this.styleConfig(this.props.style)
        var nodes = []
        for (var i = 0; i < this.props.data.length; i++) {
            var class_name = config[i]
            console.log(this.props);
            var data_elem = this.props.data[i]
            nodes.push(
                <div className={class_name}>
                    <DiploPanel title={data_elem.name} rssUrl={data_elem.rssUrl}/>
                </div>
            )
        }

        return (
            <div className="row">
                {nodes}
            </div>
        );
    }
});

var DiploPagelet = React.createClass({
    styleList: function () {
        return ['6-3-3', '3-6-3', '3-3-6', '4-8', '8-4', '4-4-4'];
    },

    loadCommentsFromServer: function () {
        console.log(this.props.url)
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            success: function (data) {
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
        // this.setState({data: [{title: 'f', description: 'd', url: 'f'}, {title: 'f', description: 'd', url: 'f'}, {title: 'f', description: 'd', url: 'f'}]});
    },

    getInitialState: function () {
        return {data: []};
    },

    componentDidMount: function () {
        this.loadCommentsFromServer();
        // setInterval(this.loadCommentsFromServer, this.props.pollInterval);
    },

    render: function () {
        var pagelet = []
        var styles = this.styleList()
        var index = Math.floor(Math.random() * styles.length);

        var style_ind = 0
        var i = 0
        while (i < this.state.data.length) {
            var style = styles[(style_ind++) % styles.length]
            if (style.length == 5) {
                pagelet.push(<DiploPanelBlock style={style} data={this.state.data.slice(i, i+3)}/>)
                i = i + 3
            } else {
                pagelet.push(<DiploPanelBlock style={style} data={this.state.data.slice(i, i+2)}/>)
                i = i + 2
            }
        }
        return (
            <div>
                {pagelet}
            </div>
        );
    }
});

var DiploPage = React.createClass({

    getInitialState: function () {
        return {
            hasMore: true,
            items: [<DiploPagelet url={url+'0'} pollInterval={5000}/>]
        };
    },

    loadMore: function (page) {
        console.log('load');
        setTimeout(function () {
            this.setState({
                items: this.state.items.concat([<DiploPagelet url={url+page.toString()} pollInterval={5000}/>]),
                hasMore: (page < 100)
            });
        }.bind(this), 1000);
    },

    render: function () {
        console.log('render');
        return (
            <InfiniteScroll loader={<div className="loader">Loading ...</div>} loadMore={this.loadMore}
                            hasMore={this.state.hasMore}>
                {this.state.items}
            </InfiniteScroll >
        );
    },
});


var token = null;

if (hello('google').getAuthResponse()) {
    token = hello('google').getAuthResponse().access_token;
}
if (token) {
    document.onload = React.render(
        <DiploPage />,
        document.getElementById('article_list')
    );
}

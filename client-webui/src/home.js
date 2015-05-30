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
        loadMore: function () {},
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

url = "http://localhost:8080/modules-java/client/feeder/feed?size=16&page="

var DiploPanel = React.createClass({
  render: function() {
    return (
        <div className="panel" style={{height: '300px'}}>
          <h5><a href={this.props.url} title={this.props.title}>{this.props.title}</a></h5>
          <p>{this.props.description}</p>
        </div>
      ); 
  }
});

var DiploPanelBlock = React.createClass({
  
  styleConfig: function(style) {
    switch (style) {
      case '6-3-3':
        return ["large-6 columns", "large-3 columns", "large-3 columns"];
      case '3-6-3':
        return ["large-3 columns", "large-6 columns", "large-3 columns"];
      case '3-3-6':
        return ["large-3 columns", "large-3 columns", "large-6 columns"];
      case '4-8':
        return ["large-4 columns", "large-8 columns"];
      case '8-4':
        return ["large-8 columns", "large-4 columns"];
      default:
        // 4-4-4
        return ["large-4 columns", "large-4 columns", "large-4 columns"];
    }
  },

  render: function() {
    var config = this.styleConfig(this.props.style)
    var nodes = []
    for (var i=0; i<this.props.data.length; i++) {
      var class_name = config[i]
      console.log(this.props);
      var data_elem = this.props.data[i]
      nodes.push(
        <div className={class_name}>
          <DiploPanel title={data_elem.title} description="" url={data_elem.url}/>
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

  loadCommentsFromServer: function() {
    console.log(this.props.url)
    $.ajax({
      url: this.props.url,
      dataType: 'json',
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },

  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    this.loadCommentsFromServer();
    // setInterval(this.loadCommentsFromServer, this.props.pollInterval);
  },

  render: function() {
    console.log('here')
    var pagelet = []
    var styles = this.styleList()
    var index = Math.floor(Math.random() * styles.length);
    
    var style_ind = 0
    var i=0
    while (i < this.state.data.length) {
      var style = styles[(style_ind++) % styles.length]
      if (style.length == 5) {
        pagelet.push(<DiploPanelBlock style={style} data={this.state.data.slice(i, i+3)}/>)
        i=i+3
      } else {
        pagelet.push(<DiploPanelBlock style={style} data={this.state.data.slice(i, i+2)}/>)
        i=i+2
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
      items: [<DiploPagelet url={url+'0'} pollInterval={5000} />]
    };
  },

  loadMore: function (page) {
    console.log('load');
    setTimeout(function () {
      this.setState({
        items: this.state.items.concat([<DiploPagelet url={url+page.toString()} pollInterval={5000} />]),
        hasMore: (page < 100)
      });
    }.bind(this), 1000);
  },

  render: function () {
    console.log('render');
    return (
      <InfiniteScroll loader={<div className="loader">Loading ...</div>} loadMore={this.loadMore} hasMore={this.state.hasMore}>
        {this.state.items}
      </InfiniteScroll > 
    );
  },
});

document.onload = React.render(
  <DiploPage />,
  document.getElementById('article_list')
);

// document.onload = React.render(
//   <DiploPagelet url={url} pollInterval={5000} />,
//   document.getElementById('article_list')
// );

// $(window).scroll(function() {
//    if($(window).scrollTop() + $(window).height() == $(document).height()) {
//        alert("bottom!");
//    }
// });
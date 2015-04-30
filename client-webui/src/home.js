function createData() {
  res = []
  for (var i=0; i<100; i++) {
    res.push({'url': 'http://habrahabr.ru/', 'title': 'Shit title ' + i, 'description': 'Shit description ' + i})
  }
  return res
}

data = createData()

url = "http://localhost:8080/modules-java/client/feeder/feed?size=50"

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
    // var title = 'Panel Title'
    // var description = 'This is a six columns'
    // var url = 'http://habrahabr.ru/'
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
    // this.setState({data: 
    //   [
    //     {
    //         id: "552ce0df0fdeea99d7d93e0f",
    //         url: "http://habrahabr.ru/post/254129/",
    //         title: "Я тебя по сетям вычислю: используем API крупнейших соцсетей в своих корыстных целях",
    //         time: "2015-04-14T11:13:41",
    //         description: "<br/><img src=\"//habrastorage.org/files/eff/fa7/a52/efffa7a52db948febafdfd32bcfde903.jpg\"/><br/>\n<br/>\nНи для кого не секрет, что современные социальные сети представляют собой огромные БД, содержащие много интересной информации о частной жизни своих пользователей. Через веб-морду особо много данных не вытянешь, но ведь у каждой сети есть свой API… Так давай же посмотрим, как этим можно воспользоваться для поиска пользователей и сбора информации о них.<br/>\n<br/>\nЕсть в американской разведке такая дисциплина, как OSINT (Open source intelligence), которая отвечает за поиск, сбор и выбор информации из общедоступных источников. К одному из крупнейших поставщиков общедоступной информации можно отнести социальные сети. Ведь практически у каждого из нас есть учетка (а у кого-то и не одна) в одной или нескольких соцсетях. Тут мы делимся своими новостями, личными фотографиями, вкусами (например, лайкая что-то или вступая в какую-либо группу), кругом своих знакомств. Причем делаем это по своей доброй воле и практически совершенно не задумываемся о возможных последствиях. На страницах журнала уже не раз рассматривали, как можно с помощью различных уловок вытаскивать из соцсетей интересные данные. Обычно для этого нужно было вручную совершить какие-то манипуляции. Но для успешной разведки логичнее воспользоваться специальными утилитами. Существует несколько open source утилит, позволяющих вытаскивать информацию о пользователях из соцсетей.<br/>\n <a href=\"http://habrahabr.ru/post/254129/#habracut\">Читать дальше &#8594;</a>"
    //     },
    //     {
    //         id: "552ce0df0fdeea99d7d93e0e",
    //         url: "http://habrahabr.ru/post/255687/",
    //         title: "[Из песочницы] Обнаружение сигнала в шумах",
    //         time: "2015-04-14T11:12:08",
    //         description: "<br/><img src=\"//habrastorage.org/files/7e4/2d7/474/7e42d7474ef747e0a125bd65d01227b6.png\"/><br/>\nПо роду своей деятельности мне приходится осуществлять контроль различных параметров наземных импульсно-фазовых радионавигационных систем (ИФРНС) «Чайка» и Loran-C. В этой статье я хочу поделиться одним из методов обнаружения времени прихода импульса ИФРНС при наличии шумов. Метод применим во многих задачах поиска сигнала известной формы.<br/>\n <a href=\"http://habrahabr.ru/post/255687/#habracut\">Читать дальше &#8594;</a>"
    //     },
    //     {
    //       id: "552ce0df0fdeea99d7d93e13",
    //       url: "http://habrahabr.ru/post/255683/",
    //       title: "[Перевод] Глубокое погружение в систему рендеринга WPF",
    //       time: "2015-04-14T10:43:34",
    //       description: "<br/><i>На перевод этой статьи меня подтолкнуло обсуждение записей <a href=\"http://habrahabr.ru/company/geekfamily/blog/253341/\">«Почему WPF живее всех живых?»</a> и <a href=\"http://habrahabr.ru/post/165273/\">«Семь лет WPF: что изменилось?»</a> Исходная статья написана в 2011 году, когда Silverlight еще был жив, но информация по WPF не потеряла актуальности.</i><br/>\n<br/>\nСначала я не хотел публиковать эту статью. Мне казалось, что это невежливо — о мертвых надо говорить либо хорошо, либо ничего. Но несколько бесед с людьми, чье мнение я очень ценю, заставили меня передумать. Вложившие много усилий в платформу Microsoft разработчики должны знать о внутренних особенностях ее работы, чтобы, зайдя в тупик, они могли понимать причины произошедшего и более точно формулировать пожелания к разработчикам платформы. Я считаю WPF и Silverlight хорошими технологиями, но… Если вы следили за моим Twitter последние несколько месяцев, то некоторые высказывания могли показаться вам безосновательными нападками на производительность WPF и Silverlight. Почему я это писал? Ведь, в конце концов, я вложил тысячи и тысячи часов моего собственного времени в течение многих лет, пропагандируя платформу, разрабатывая библиотеки, помогая участникам сообщества и так далее. Я однозначно лично заинтересован. Я хочу, чтобы платформа стала лучше.<br/>\n<br/>\n<img src=\"//habrastorage.org/files/82d/b5e/62d/82db5e62d4524237975836e5b2bceea9.png\"/><br/>\n <a href=\"http://habrahabr.ru/post/255683/#habracut\">Читать дальше &#8594;</a>"
    //     }
    //   ]
    // });
  },

  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    this.loadCommentsFromServer();
    setInterval(this.loadCommentsFromServer, this.props.pollInterval);
  },

  render: function() {
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


// DEPRECATED
var DiploArticle = React.createClass({
  render: function() {
    return (
        <article>
          <h1><a href={this.props.url} title={this.props.title}>TITLE</a></h1>
          <img src="" alt="" />
          <p>{this.props.description}</p>
        </article>
      ); 
  }
});

// DEPRECATED
var DiploArticleList = React.createClass({
  render: function() {
    var nodes = this.props.data.map(function(item) {
      return (
        <DiploArticle url={item.url} title={item.title} description={item.description}/>
      );
    });
    return (
      <div className="article_list_component">
        {nodes}
      </div>
    );
  }
});

React.render(
  <DiploPagelet url={url} pollInterval={5000} />,
  document.getElementById('article_list')
);
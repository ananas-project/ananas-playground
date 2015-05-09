/*******************************************************************************
 * blz.mini.js
 */

function blz_mini_js() {
}

/*******************************************************************************
 * class BLZMini - the tools class
 */

function BLZMini() {
}

BLZMini.packagePath = function(value) {
	return BLZMini.property('PACKAGE_PATH', value);
};

BLZMini.property = function(key, value) {
	return BLZMiniPage.getInstance().property(key, value);
};

BLZMini.normalBoxName = function(name) {
	var sb = "";
	var len = name.length;
	for (var i = 0; i < len; ++i) {
		var ch = name.charAt(i);
		var ch2 = ch;
		if (ch == ' ') {
			continue;
		} else if (ch == '\\') {
			return null;
		} else if (ch == '/') {
			return null;
		} else if ('0' <= ch && ch <= '9') {
		} else if ('0' <= ch && ch <= '9') {
		} else if ('a' <= ch && ch <= 'z') {
		} else if ('A' <= ch && ch <= 'Z') {
		} else if (ch == '-' || ch == '_') {
		} else {
			continue;
		}
		if (ch2 != null) {
			sb = sb + ch2;
		}
	}
	return sb.toLowerCase();
};

BLZMini.loadPageFramework = function(sel_parent, sel_child) {

	var parent0 = $(sel_parent);
	var child0 = $(sel_child);
	var page = BLZMiniPage.getInstance();

	// meta
	var node_vpt = $('<meta name="viewport"  />');
	{
		var content = "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;";
		node_vpt.attr('content', content);
	}

	// icon & title
	var icon_url = page.normalPath('/lib/image/snow_48.png');
	var node_icon = $('<link rel="icon" />');
	var node_title = $('title');
	node_title.text("菠萝斋·雪");
	node_icon.attr('href', icon_url);
	node_title.after(node_icon);
	node_title.before(node_vpt);

	// load header
	page.loadHtml("/lib/fragment/header.html", function(html) {

		var root = html.find('#page-framework-root');
		var hdr = root.find('#page-framework-header');
		var clt = root.find('#page-framework-client');
		var ftr = root.find('#page-framework-footer');

		root.appendTo(parent0);
		child0.appendTo(clt);

		// logo
		var homeURL = page.normalPath('/');
		var logoURL = page.normalPath('/lib/image/snow_48.png');
		hdr.find(".image-logo").attr('src', logoURL);
		hdr.find('.home-page-link').attr('href', homeURL);

		// find box
		var on_find_box = function() {
			var boxName = hdr.find('#edit-find-box').val();
			boxName = BLZMini.normalBoxName(boxName);
			if (boxName == null) {
				alert("请输入有效的盒子名称。");
			} else {
				var url = '/box/' + boxName;
				// alert("nav2 " + url);
				BLZMiniPage.getInstance().nav2(url);
			}
		};
		hdr.find('#btn-find-box').click(on_find_box);
		hdr.find('#edit-find-box').keydown(function(event) {
			var key = event.keyCode;
			// alert("key = " + key);
			if (key == '13') {
				on_find_box();
			}
		});

		// account area
		var loginURL = page.normalPath('/login');
		var regURL = page.normalPath('/register');
		hdr.find('.login-page-link').attr('href', loginURL);
		hdr.find('.register-page-link').attr('href', regURL);

	});

};

/*******************************************************************************
 * class BLZMiniPage - the page context of boluozhai-mini
 */

function BLZMiniPage() {
}

BLZMiniPage.getInstance = function() {
	var inst = BLZMiniPage.__s_inst__;
	if (inst == null) {
		inst = new BLZMiniPage();
		BLZMiniPage.__s_inst__ = inst;
	}
	return inst;
};

BLZMiniPage.prototype.property = function(key, value) {
	var tab = this.__properties__;
	if (tab == null) {
		this.__properties__ = tab = {};
	}
	if (value == null) {
		return tab[key];
	} else {
		tab[key] = value;
		return value;
	}
};

BLZMiniPage.prototype.loadHtml = function(url, callback_html) {
	url = this.normalPath(url);
	var root = $('<div/>');
	root.load(url, function(text, status, xhr) {
		if (status == "success") {
			var html = root; // $(text);
			callback_html(html);
		}
	});
};

BLZMiniPage.prototype.pathToBase = function() {
	var k = "__the_path_to_base__";
	var v = this.property(k);
	if (v == null) {
		var pp = BLZMini.packagePath();
		var array = pp.split('/');
		v = '.';
		for ( var i in array) {
			var str = $.trim(array[i]);
			if (str.length > 0) {
				v = v + '/..';
			}
		}
		this.property(k, v);
	}
	return v;
};

BLZMiniPage.prototype.nav2 = function(url) {
	url = this.normalPath(url);
	window.location = url;
};

BLZMiniPage.prototype.normalPath = function(path) {
	if (path.indexOf('/') == 0) {
		var p2b = this.pathToBase();
		path = (p2b + path);
	}
	return path;
};

/*******************************************************************************
 * EOF
 */

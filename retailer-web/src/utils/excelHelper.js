/* eslint-disable no-empty */
/* eslint-disable no-redeclare */
import XLSX from "xlsx";
import XLSXS from "xlsx-style";
import FileSaver from 'file-saver';

export function fileToExcel(file,header) {
	return new Promise(function (resolve) {
		const reader = new FileReader()
		reader.onload = function (e) {
			const data = e.target.result
			this.wb = XLSX.read(data, {
				type: 'binary'
			})
			const result = []
			this.wb.SheetNames.forEach((sheetName) => {
				result.push({
					sheetName: sheetName,
					sheet:header? XLSX.utils.sheet_to_json(this.wb.Sheets[sheetName],{header:1,defval:''}):XLSX.utils.sheet_to_json(this.wb.Sheets[sheetName])
				})
			})
			resolve(result)
		}
		reader.readAsBinaryString(file.raw)
		// reader.readAsBinaryString(file) // 传统input方法
	})

}

/**
 * 
 * @param {*内容(json数组)，表头，是否有边框，sheet名称,结尾} param0 
 * {json:json,header:[{value:"报表"}],keys:keys,border:true,sheetName:'sheet1',fileName:'报表1'}
 */
export function export_json_to_excel({ json, header,keys, border, sheetName,type,fileName}) {
	var tmpdata = json[0];
	json.unshift({});
	header.map(() => {
		json.unshift({})
	})
	var keyMap = []; //获取keys
	for (var k in tmpdata) {
		keyMap.push(k);
		var colunmKey=keys[k]
		if(colunmKey){
			json[header.length][k] = colunmKey;
		}else{
			json[header.length][k] = k;
		}		
	}
	var borderStyle= { top: { style: 'thin' },bottom: { style: 'thin' },left: { style: 'thin' },right: { style: 'thin' }}
	var contentCellStyle = border ? {font:{sz: 12},border:borderStyle,alignment: {
		horizontal: "center" ,
		vertical: "center"
	  }} : {font: {sz: 12}};
	var tableHeaderStyle= {font:{sz: 16,color: {rgb: '00000000'}},fill: { bgColor: { indexed: 64 }, fgColor: { rgb: "00A9A9A9" } },border:borderStyle,alignment: {
		horizontal: "center" ,
		vertical: "center"
	  }}
	var tmpdata = [];//用来保存转换好的json 
	var tableHeaderIndex=header.length+1;
	json.map((v, i) => keyMap.map((k, j) => Object.assign({}, {
		v: v[k],
		position: (j > 25 ? getCharCol(j) : String.fromCharCode(65 + j)) + (i + 1),
		char:getCharCol(j)
	}))).reduce((prev, next) => prev.concat(next)).forEach((v) => {
		var key=v.char+tableHeaderIndex
		if(v.position==key){
			tmpdata[v.position] = {
				v: v.v,
				s:tableHeaderStyle
		}		
		}else{
			tmpdata[v.position] = {
				v: v.v,
				s: contentCellStyle
		}	
	}});
	var t="A"+(header.length+1)
	tmpdata[t].s=tableHeaderStyle
	var outputPos = Object.keys(tmpdata); //设置区域,比如表格从A1到D10
	var merges=[]
    for (let index = 0; index < header.length; index++) {
		var key="A"+(index+1);
		tmpdata[key]={v:header[index].value}
		var merge={
			s:{r:index,c:0},
			e:{r:index,c:keyMap.length-1}
		}
		merges.push(merge)
		if(index==0){
			tmpdata[key].s={font: { sz: 18, bold: true, color: { rgb: "FFFFAA00" }},alignment: {
				horizontal: "center" ,
				vertical: "center"
			  },border:borderStyle}
		}else{
			tmpdata[key].s={font: { sz: 16, bold: true, color: { rgb: "FFFFAA00" }},alignment: {
				horizontal: "right" ,
				vertical: "center"
			  },border:borderStyle}
		}	
	}
	tmpdata["!merges"]=merges
	var tmpWB = {
		SheetNames: [sheetName], //保存的表标题
		Sheets: {
			[sheetName]: Object.assign({},
				tmpdata, //内容
				{
					'!ref': outputPos[0] + ':' + outputPos[outputPos.length - 1] //设置填充区域
				})
		}
	};
	var tmpDown = new Blob([s2ab(XLSXS.write(tmpWB,
		{ bookType: (type == undefined ? 'xlsx' : type), bookSST: false, type: 'binary' }//这里的数据是用来定义导出的格式类型
	))], {
		type: ""
	});
	var wopts = { bookType: 'xlsx', bookSST: true, type: 'binary', cellStyles: true };
	saveAs(tmpDown, fileName + '.' + (wopts.bookType == "biff2" ? "xls" : wopts.bookType));
}

function s2ab(s) {
	if (typeof ArrayBuffer !== "undefined") {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xff;
		return buf;
	} else {
		var buf = new Array(s.length);
		for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xff;
		return buf;
	}
}
// 下载功能
function saveAs(obj, fileName) {
	var tmpa = document.createElement("a");
	tmpa.download = fileName || "未命名";
	// 兼容ie 火狐 下载文件
	if ("msSaveOrOpenBlob" in navigator) {
		window.navigator.msSaveOrOpenBlob(obj, fileName);
	} else if (window.navigator.userAgent.includes("Firefox")) {
		var a = document.createElement("a");
		a.href = URL.createObjectURL(obj);
		a.download = fileName;
		document.body.appendChild(a);
		a.click();
	} else {
		tmpa.href = URL.createObjectURL(obj);
	}
	tmpa.click();
	setTimeout(function () {
		URL.revokeObjectURL(obj);
	}, 100);
}

function getCharCol(n) {
	let s = "",
		m = 0;
	while (n > 0) {
		m = (n % 26) + 1;
		s = String.fromCharCode(m + 64) + s;
		n = (n - m) / 26;
	}
	return s;
}


export function arrayToLocalFile(jsonArray, fileName, sheetName) {
	var wopts = {
		bookType: 'xlsx',
		bookSST: true,
		type: 'binary'
	};
	var workBook = {
		SheetNames: [typeof sheetName == 'undefined' ? "sheet1" : sheetName],
		Sheets: {},
		Props: {}
	};
	//1、XLSX.utils.json_to_sheet(data) 接收一个对象数组并返回一个基于对象关键字自动生成的“标题”的工作表，默认的列顺序由使用Object.keys的字段的第一次出现确定
	//2、将数据放入对象workBook的Sheets中等待输出
	workBook.Sheets[sheetName] = XLSX.utils.json_to_sheet(jsonArray);

	//3、XLSX.write() 开始编写Excel表格
	//4、changeData() 将数据处理成需要输出的格式
	FileSaver.saveAs(new Blob([changeData(XLSX.write(workBook, wopts))], { type: 'application/octet-stream' }), fileName)
}
function changeData(s) {
	//如果存在ArrayBuffer对象(es6) 最好采用该对象
	if (typeof ArrayBuffer !== 'undefined') {

		//1、创建一个字节长度为s.length的内存区域
		var buf = new ArrayBuffer(s.length);

		//2、创建一个指向buf的Unit8视图，开始于字节0，直到缓冲区的末尾
		var view = new Uint8Array(buf);

		//3、返回指定位置的字符的Unicode编码
		for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
		return buf;

	} else {
		var buf = new Array(s.length);
		for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	}
}




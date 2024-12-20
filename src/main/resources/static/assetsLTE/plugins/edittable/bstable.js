// /*
//  * BSTable
//  * @description  Javascript (JQuery) library to make bootstrap tables editable. Inspired by Tito Hinostroza's library Bootstable. BSTable Copyright (C) 2020 Thomas Rokicki
//  *
//  * @version 1.0
//  * @author Thomas Rokicki (CraftingGamerTom), Tito Hinostroza (t-edson)
//  */
//
// "use strict";
//
// /** @class BSTable class that represents an editable bootstrap table. */
// class BSTable {
//
//   /**
//    * Creates an instance of BSTable.
//    *
//    * @constructor
//    * @author: Thomas Rokicki (CraftingGamerTom)
//    * @param {tableId} tableId The id of the table to make editable.
//    * @param {options} options The desired options for the editable table.
//    */
//   constructor(tableId, options) {
//
//     var defaults = {
//       editableColumns: null,          // Index to editable columns. If null all td will be editable. Ex.: "1,2,3,4,5"
//       $addButton: null,               // Jquery object of "Add" button
//       onEdit: function() {},          // Called after edition
//       onBeforeDelete: function() {},  // Called before deletion
//       onDelete: function() {},        // Called after deletion
//       onAdd: function() {},           // Called when added a new row
//       advanced: {                     // Do not override advanced unless you know what youre doing
//           columnLabel: 'Actions',
//           buttonHTML: `<div class="btn-group pull-right">
//                 <button id="bEdit" type="button" class="btn btn-sm btn-default">
//                     <span  class="fa fa-edit" > </span>
//                 </button>
//                 <button id="bDel" type="button" class="btn btn-sm btn-default">
//                     <span class="fa fa-trash" > </span>
//                 </button>
//                 <button id="bAcep" type="submit" class="btn btn-sm btn-default" style="display:none;">
//                     <span class="fa fa-check-circle" > </span>
//                 </button>
//                 <button id="bCanc" type="button" class="btn btn-sm btn-default" style="display:none;">
//                     <span class="fa fa-times-circle" > </span>
//                 </button>
//             </div>`
//       }
//     };
//
//     this.table = $('#' + tableId);
//     this.options = $.extend(true, defaults, options);
//
//     /** @private */ this.actionsColumnHTML = '<td name="bstable-actions">' + this.options.advanced.buttonHTML + '</td>';
//   }
//
//   // --------------------------------------------------
//   // -- Public Functions
//   // --------------------------------------------------
//
//   /**
//    * Initializes the editable table. Creates the actions column.
//    * @since 1.0.0
//    */
//   init() {
//     this.table.find('thead tr').append('<th name="bstable-actions">' + this.options.advanced.columnLabel + '</th>');  // Append column to header
//     this.table.find('tbody tr').append(this.actionsColumnHTML);
//
//     this._addOnClickEventsToActions(); // Add onclick events to each action button in all rows
//
//     // Process "addButton" parameter
//     if (this.options.$addButton != null) {
//       let _this = this;
//       // Add a managed onclick event to the button
//       this.options.$addButton.click(function() {
//         _this._actionAddRow();
//       });
//     }
//     //Process "editableColumns" parameter. Sets the columns that will be editable
//     if (this.options.editableColumns != null) {
//       //Extract felds
//       this.options.editableColumns = this.options.editableColumns.split(',');
//     }
//   }
//
//   /**
//    * Destroys the editable table. Removes the actions column.
//    * @since 1.0.0
//    */
//   destroy() {
//     this.table.find('th[name="bstable-actions"]').remove(); //remove header
//     this.table.find('td[name="bstable-actions"]').remove(); //remove body rows
//   }
//
//   /**
//    * Refreshes the editable table.
//    *
//    * Literally just removes and initializes the editable table again, wrapped in one function.
//    * @since 1.0.0
//    */
//   refresh() {
//     this.destroy();
//     this.init();
//   }
//
//   // --------------------------------------------------
//   // -- 'Static' Functions
//   // --------------------------------------------------
//
//   /**
//    * Returns whether the provided row is currently being edited.
//    *
//    * @param {Object} row
//    * @return {boolean} true if row is currently being edited.
//    * @since 1.0.0
//    */
//   currentlyEditingRow($row) {
//     // Check if the row is currently being edited
//     if ($row.attr('data-status')=='editing') {
//         return true;
//     } else {
//         return false;
//     }
//   }
//
//   // --------------------------------------------------
//   // -- Button Mode Functions
//   // --------------------------------------------------
//
//   _actionsModeNormal(button) {
//     $(button).parent().find('#bAcep').hide();
//     $(button).parent().find('#bCanc').hide();
//     $(button).parent().find('#bEdit').show();
//     $(button).parent().find('#bDel').show();
//     var $row = $(button).parents('tr');         // get the row
//     $row.attr('data-status', '');               // remove editing status
//   }
//   _actionsModeEdit(button) {
//     $(button).parent().find('#bAcep').show();
//     $(button).parent().find('#bCanc').show();
//     $(button).parent().find('#bEdit').hide();
//     $(button).parent().find('#bDel').hide();
//     var $row = $(button).parents('tr');         // get the row
//     $row.attr('data-status', 'editing');        // indicate the editing status
//   }
//
//   // --------------------------------------------------
//   // -- Private Event Functions
//   // --------------------------------------------------
//
//   _rowEdit(button) {
//   // Indicate user is editing the row
//     var $row = $(button).parents('tr');       // access the row
//     console.log($row);
//     var $cols = $row.find('td');              // read rows
//     console.log($cols);
//     if (this.currentlyEditingRow($row)) return;    // not currently editing, return
//     //Pone en modo de edición
//     this._modifyEachColumn(this.options.editableColumns, $cols, function($td) {  // modify each column
//       var content = $td.html();             // read content
//       console.log(content);
//       var div = '<div style="display: none;">' + content + '</div>';  // hide content (save for later use)
//       var input = '<input class="form-control input-sm"  data-original-value="' + content + '" value="' + content + '">';
//       $td.html(div + input);                // set content
//     });
//     this._actionsModeEdit(button);
//   }
//   _rowDelete(button) {
//   // Remove the row
//     if (confirm('Bạn có chắc chắn muốn xóa khách hàng này?')) {
//       var $row = $(button).parents('tr');       // access the row
//       this.options.onBeforeDelete($row);
//       $row.remove();
//       this.options.onDelete();
//       // Gọi API xóa hoặc thực hiện hành động xóa
//       fetch(`/admin/delete/${userid}`, {
//         method: 'DELETE'
//       }).then(response => {
//         if (response.ok) {
//           location.reload(); // Tải lại trang để cập nhật danh sách
//         }
//       });
//     }
//   }
//   _rowAccept(button) {
//   // Accept the changes to the row
//     var $row = $(button).parents('tr');       // access the row
//     var $cols = $row.find('td');              // read fields
//     if (!this.currentlyEditingRow($row)) return;   // not currently editing, return
//
//     // Lưu thay đổi khi nhấn "Accept"
//     this._modifyEachColumn(this.options.editableColumns, $cols, function($td) {  // modify each column
//       var cont = $td.find('input').val();     // read through each input
//       $td.html(cont);                         // set the content and remove the input fields
//     });
//     this._actionsModeNormal(button);
//     this.options.onEdit($row);
//   }
//   _rowCancel(button) {
//   // Reject the changes
//     var $row = $(button).parents('tr');       // access the row
//     var $cols = $row.find('td');              // read fields
//     if (!this.currentlyEditingRow($row)) return;   // not currently editing, return
//
//     // Hủy thay đổi khi nhấn "Cancel"
//     this._modifyEachColumn(this.options.editableColumns, $cols, function($td) {  // modify each column
//         var cont = $td.find('div').html();    // read div content
//         $td.html(cont);                       // set the content and remove the input fields
//     });
//     this._actionsModeNormal(button);
//   }
//   _actionAddRow() {
//     // Add row to this table
//
//     var $allRows = this.table.find('tbody tr');
//     if ($allRows.length==0) { // there are no rows. we must create them
//       var $row = this.table.find('thead tr');  // find header
//       var $cols = $row.find('th');  // read each header field
//       // create the new row
//       var newColumnHTML = '';
//       $cols.each(function() {
//         let column = this; // Inner function this (column object)
//         if ($(column).attr('name')=='bstable-actions') {
//           newColumnHTML = newColumnHTML + actionsColumnHTML;  // add action buttons
//         } else {
//           newColumnHTML = newColumnHTML + '<td></td>';
//         }
//       });
//       this.table.find('tbody').append('<tr>'+newColumnHTML+'</tr>');
//     } else { // there are rows in the table. We will clone the last row
//       var $lastRow = this.table.find('tr:last');
//       $lastRow.clone().appendTo($lastRow.parent());
//       $lastRow = this.table.find('tr:last');
//       var $cols = $lastRow.find('td');  //lee campos
//       $cols.each(function() {
//         let column = this; // Inner function this (column object)
//         if ($(column).attr('name')=='bstable-actions') {
//           // action buttons column. change nothing
//         } else {
//           $(column).html('');  // clear the text
//         }
//       });
//     }
//     this._addOnClickEventsToActions(); // Add onclick events to each action button in all rows
//     this.options.onAdd();
//   }
//
//   // --------------------------------------------------
//   // -- Helper Functions
//   // --------------------------------------------------
//
//   _modifyEachColumn($editableColumns, $cols, howToModify) {
//   // Go through each editable field and perform the howToModifyFunction function
//     var n = 0;
//     $cols.each(function() {
//       n++;
//       if ($(this).attr('name')=='bstable-actions') return;    // exclude the actions column
//       if (!isEditableColumn(n-1)) return;                     // Check if the column is editable
//       howToModify($(this));                                   // If editable, call the provided function
//     });
//     // console.log("Number of modified columns: " + n); // debug log
//
//
//     function isEditableColumn(columnIndex) {
//     // Indicates if the column is editable, based on configuration
//         if ($editableColumns==null) {                           // option not defined
//             return true;                                        // all columns are editable
//         } else {                                                // option is defined
//             //console.log('isEditableColumn: ' + columnIndex);  // DEBUG
//             for (var i = 0; i < $editableColumns.length; i++) {
//               if (columnIndex == $editableColumns[i]) return true;
//             }
//             return false;  // column not found
//         }
//     }
//   }
//
//   _addOnClickEventsToActions() {
//     let _this = this;
//     // Add onclick events to each action button
//     this.table.find('tbody tr #bEdit').each(function() {let button = this; button.onclick = function() {_this._rowEdit(button)} });
//     this.table.find('tbody tr #bDel').each(function() {let button = this; button.onclick = function() {_this._rowDelete(button)} });
//     this.table.find('tbody tr #bAcep').each(function() {let button = this; button.onclick = function() {_this._rowAccept(button)} });
//     this.table.find('tbody tr #bCanc').each(function() {let button = this; button.onclick = function() {_this._rowCancel(button)} });
//   }
//
//   // --------------------------------------------------
//   // -- Conversion Functions
//   // --------------------------------------------------
//
//   convertTableToCSV(separator) {
//   // Convert table to CSV
//     let _this = this;
//     var rowValues = '';
//     var tableValues = '';
//
//     _this.table.find('tbody tr').each(function() {
//         // force edits to complete if in progress
//         if (_this.currentlyEditingRow($(this))) {
//             $(this).find('#bAcep').click();       // Force Accept Edit
//         }
//         var $cols = $(this).find('td');           // read columns
//         rowValues = '';
//         $cols.each(function() {
//             if ($(this).attr('name')=='bstable-actions') {
//                 // buttons column - do nothing
//             } else {
//                 rowValues = rowValues + $(this).html() + separator;
//             }
//         });
//         if (rowValues!='') {
//             rowValues = rowValues.substr(0, rowValues.length-separator.length);
//         }
//         tableValues = tableValues + rowValues + '\n';
//     });
//     return tableValues;
//   }
//
// }
//
function editRow(button) {
  const row = button.closest("tr");
  const inputs = row.querySelectorAll('input[type="text"], select, textarea'); // Chọn cả input và select

  // Chuyển đổi trường nhập thành có thể chỉnh sửa
  inputs.forEach(input => input.disabled = false);

  // Hiện nút Chấp nhận và Hủy, ẩn nút Chỉnh sửa
  button.style.display = "none";
  row.querySelector(".bAcep").style.display = "inline-block";
  row.querySelector(".bCanc").style.display = "inline-block";

  // Ẩn nút Xóa
  row.querySelector(".bDel").style.display = "none"; // Ẩn nút Xóa

  // Hiển thị thẻ chọn ảnh
  const fileInput = row.querySelector('input[type="file"]'); // Lấy thẻ input file
  if (fileInput) {
    fileInput.disabled = false; // Cho phép chọn ảnh
    fileInput.style.display = "block"; // Hiện thẻ chọn ảnh
  }
}



function cancelEdit(button) {
  const row = button.closest("tr");
  const inputs = row.querySelectorAll('input[type="text"],select, textarea');

  // Khôi phục trạng thái không thể chỉnh sửa
  inputs.forEach(input => input.disabled = true);

  // Hiện nút Chỉnh sửa, ẩn nút Chấp nhận và Hủy
  row.querySelector(".bEdit").style.display = "inline-block";
  button.style.display = "none";
  row.querySelector(".bAcep").style.display = "none";

  row.querySelector(".bDel").style.display = "inline-block";

  // Hiển thị thẻ chọn ảnh
  const fileInput = row.querySelector('input[type="file"]'); // Lấy thẻ input file
  if (fileInput) {
    fileInput.disabled = false; // Cho phép chọn ảnh
    fileInput.style.display = "none"; // Hiện thẻ chọn ảnh
  }
}

// function confirmDelete(userid) {
//   if (confirm('Bạn có chắc chắn muốn xóa khách hàng này aaa?')) {
//     fetch(`/admin/delete/${userid}`, {
//       method: 'DELETE'
//     })
//         .then(response => {
//           if (response.ok) {
//             location.reload();
//           } else {
//             response.json().then(data => {
//               alert(data.message || 'Xóa không thành công. Vui lòng thử lại.');
//             });
//           }
//         })
//         .catch(error => {
//           console.error('Có lỗi xảy ra:', error);
//           alert('Có lỗi hệ thống. Vui lòng liên hệ quản trị viên.');
//         });
//   }
// }

